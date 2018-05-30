package victor.training.functional.patterns;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;

// get the products frequently ordered during the past year



class ProductService {
	private ProductRepo productRepo;

	public List<Product> getFrequentOrderedProducts(List<Order> orders) {
		return getProductCountsOverTheLastYear(orders).entrySet().stream()
				.filter(e -> e.getValue() >= 10)
				.map(Entry::getKey)
				.filter(Product::isNotDeleted)
				.filter(p -> !productRepo.getHiddenProductIds().contains(p.getId()))
				.collect(toList());
	}

	private Map<Product, Integer> getProductCountsOverTheLastYear(List<Order> orders) {
		return orders.stream()
				.filter(o -> o.getCreationDate().isAfter(LocalDate.now().minusYears(1)))
				.flatMap(o -> o.getOrderLines().stream())
				.collect(groupingBy(OrderLine::getProduct, summingInt(OrderLine::getItemCount)));
	}
}





//VVVVVVVVV ==== supporting (dummy) code ==== VVVVVVVVV
@Data
class Product {
	private Long id;
	private boolean deleted;
	
	public boolean isNotDeleted() {
		return !deleted;
	}
}

@Data
class Order {
	private Long id;
	private List<OrderLine> orderLines;
	private LocalDate creationDate;
}

@Data
class OrderLine {
	private Product product;
	private int itemCount;
}

interface ProductRepo {
	List<Long> getHiddenProductIds();
}
