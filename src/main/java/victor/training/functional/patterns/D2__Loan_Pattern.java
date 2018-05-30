package victor.training.functional.patterns;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.extern.slf4j.Slf4j;

// export all orders to a file

interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
@Slf4j
class OrderExporter {
	
	private OrderRepo repo;
			
	public File exportFile(String fileName) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			writer.write("OrderID;Date\n");
			repo.findByActiveTrue()
				.map(o -> o.getId() + ";" + o.getCreationDate())
				.forEach(t -> {
					try {
						writer.write(t);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}
}

