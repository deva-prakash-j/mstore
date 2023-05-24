package com.mstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.mstore.exceptios.BadRequestException;
import com.mstore.model.QueryModel;

@Service
public class FilterBuilderService {

  private static final int DEFAULT_SIZE_PAGE = 12;

  private static final Map<String, Function<QueryModel, Criteria>> FILTER_CRITERIA =
      new HashMap<>();

  static {
    FILTER_CRITERIA.put("EQUAL",
        condition -> Criteria.where(condition.getField()).is(condition.getValue()));
    FILTER_CRITERIA.put("NOT_EQUAL",
        condition -> Criteria.where(condition.getField()).ne(condition.getValue()));
    FILTER_CRITERIA.put("GREATER_THAN",
        condition -> Criteria.where(condition.getField()).gt(condition.getValue()));
    FILTER_CRITERIA.put("GREATER_THAN_OR_EQUAL_TO",
        condition -> Criteria.where(condition.getField()).gte(condition.getValue()));
    FILTER_CRITERIA.put("LESS_THAN",
        condition -> Criteria.where(condition.getField()).lt(condition.getValue()));
    FILTER_CRITERIA.put("LESSTHAN_OR_EQUAL_TO",
        condition -> Criteria.where(condition.getField()).lte(condition.getValue()));
    FILTER_CRITERIA.put("CONTAINS", condition -> Criteria.where(condition.getField())
        .regex((String) condition.getValue(), "i"));
    FILTER_CRITERIA.put("JOIN",
        condition -> Criteria.where(condition.getField()).is(condition.getValue()));
  }

  public PageRequest getPageable(int size, int page, String order) {

    int pageSize = (size <= 0) ? DEFAULT_SIZE_PAGE : size;
    int currentPage = (page <= 0) ? 1 : page;

    try {
      if (order != null && !order.isEmpty()) {

        final String FILTER_CONDITION_DELIMITER = "\\|";

        List<String> values = split(order, FILTER_CONDITION_DELIMITER);
        String column = values.get(0);
        String sortDirection = values.get(1);

        if (sortDirection.equalsIgnoreCase("ASC")) {
          return PageRequest.of((currentPage - 1), pageSize, Sort.by(Sort.Direction.ASC, column));
        } else if (sortDirection.equalsIgnoreCase("DESC")) {
          return PageRequest.of((currentPage - 1), pageSize, Sort.by(Sort.Direction.DESC, column));
        } else {
          throw new IllegalArgumentException(
              String.format("Value for param 'order' is not valid : %s , must be 'asc' or 'desc'",
                  sortDirection));
        }

      } else {
        return PageRequest.of((currentPage - 1), pageSize);
      }
    } catch (Exception ex) {
      throw new BadRequestException("Cannot create condition filter " + ex.getMessage());
    }
  }

  public Query generateFilterQuery(List<QueryModel> userQueries) {
    if (userQueries == null || userQueries.isEmpty()) {
      return new Query();
    }
    List<Criteria> filteConditions = new ArrayList();
    userQueries.stream().map(condition -> filteConditions.add(buildCriteria(condition)))
        .collect(Collectors.toList());
    Criteria criteria = new Criteria();
    return new Query(criteria.andOperator(filteConditions.toArray(new Criteria[0])));
  }

  private static List<String> split(String search, String delimiter) {
    return Stream.of(search.split(delimiter)).collect(Collectors.toList());
  }

  private Criteria buildCriteria(QueryModel condition) {
    Function<QueryModel, Criteria> function = FILTER_CRITERIA.get(condition.getOperator().name());

    if (function == null) {
      throw new IllegalArgumentException("Invalid function param type: ");
    }

    return function.apply(condition);
  }
}
