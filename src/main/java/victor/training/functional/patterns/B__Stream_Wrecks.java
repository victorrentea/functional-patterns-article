package victor.training.functional.patterns;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;

// get the products frequently ordered during the past year



class ProductService {
	private ProductRepo productRepo;

	public List<Product> getFrequentOrderedProducts(List<Order> orders) {
		List<Long> hiddenProductIds = productRepo.getHiddenProductIds();
		Predicate<Product> productIsNotHidden = p -> !hiddenProductIds.contains(p.getId());
		Stream<Product> frequentProducts = getProductCountsOverTheLastYear(orders).entrySet().stream()
				.filter(e -> e.getValue() >= 10)
				.map(Entry::getKey);
		return frequentProducts
				.filter(Product::isNotDeleted)
				.filter(productIsNotHidden)
				.collect(toList());
	}

	private Map<Product, Integer> getProductCountsOverTheLastYear(List<Order> orders) {
		Predicate<Order> inThePreviousYear = o -> o.getCreationDate().isAfter(LocalDate.now().minusYears(1));
		return orders.stream()
				.filter(inThePreviousYear)
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
@AllArgsConstructor
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
