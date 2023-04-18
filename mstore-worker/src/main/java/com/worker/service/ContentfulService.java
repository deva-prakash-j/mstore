package com.worker.service;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAAssetFile;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;
import com.contentful.java.cma.model.CMAUpload;

@Service
public class ContentfulService {

  private final CMAClient cmaClient;
  private final CDAClient cdaClient;

  public ContentfulService(
      @Value("${contentful.management.api.token}") final String cfManagementToken,
      @Value("${contentful.delivery.api.token}") final String cfDeliveryToken,
      @Value("${contentful.space.id}") final String cfSpaceId,
      @Value("${contentful.environment.id}") final String cfEnvironmentId) {
    this.cmaClient =
        new CMAClient.Builder().setAccessToken(cfManagementToken).setSpaceId(cfSpaceId).build();
    this.cdaClient = CDAClient.builder().setSpace(cfSpaceId).setEnvironment(cfEnvironmentId)
        .setToken(cfDeliveryToken).build();
  }

  public String uploadImage(String imgUrl, String title) {
    try {
      URL urlObj = new URL(imgUrl);
      InputStream stream = this.urlToInputStream(urlObj);
      if (stream != null) {
        CMAUpload cmaUpload = this.cmaClient.uploads().create(stream);
        CMAAsset asset = new CMAAsset();
        asset.getFields().setTitle("en-US", title);

        CMAAssetFile file =
            new CMAAssetFile().setUploadFrom(new CMALink(CMAType.Upload).setId(cmaUpload.getId()))
                .setFileName(title + ".jpg").setContentType("image/jpeg");
        asset.getFields().setFile("en-US", file);
        asset = this.cmaClient.assets().create(asset);

        this.cmaClient.assets().process(asset, "en-US");
        // wait till processing is done
        String url = asset.getFields().localize("en-US").getFile().getUrl();
        int attempts = 10;
        while (attempts > 0 && (url == null || url.length() == 0)) {
          asset = this.cmaClient.assets().fetchOne(asset.getId());
          url = asset.getFields().localize("en-US").getFile().getUrl();
          attempts--;

          try {
            Thread.sleep(500);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        if (attempts <= 0) {
          throw new IllegalStateException("Could not finish processing for " + asset);
        }
        this.cmaClient.assets().publish(asset);

        CDAAsset createdAsset = this.cdaClient.fetch(CDAAsset.class).one(asset.getId());
        return createdAsset.url();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private InputStream urlToInputStream(URL url) {
    HttpsURLConnection con = null;
    InputStream inputStream = null;
    try {
      con = (HttpsURLConnection) url.openConnection();
      con.setConnectTimeout(15000);
      con.setReadTimeout(15000);
      con.connect();
      int responseCode = con.getResponseCode();
      if (responseCode < 400 && responseCode > 299) {
        String redirectUrl = con.getHeaderField("Location");
        try {
          URL newUrl = new URL(redirectUrl);
          return urlToInputStream(newUrl);
        } catch (MalformedURLException e) {
          URL newUrl = new URL(url.getProtocol() + "://" + url.getHost() + redirectUrl);
          return urlToInputStream(newUrl);
        }
      }
      /* !!!!! */

      inputStream = con.getInputStream();
      return inputStream;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
