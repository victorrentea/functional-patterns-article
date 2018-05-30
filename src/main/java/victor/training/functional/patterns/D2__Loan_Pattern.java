package victor.training.functional.patterns;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.stream.Stream;

import org.jooq.lambda.Unchecked;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.extern.slf4j.Slf4j;

// export all orders to a file

interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
@Slf4j
class OrderExporter {
	
	public enum ExportType {
		ORDER, USER
	}
	
	private OrderRepo orderRepo;
	
	private UserRepo userRepo; 
			
	public File exportFile(String fileName, ExportType exportType) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			if (exportType == ExportType.ORDER) {
				writer.write("OrderID;Date\n");
				orderRepo.findByActiveTrue()
					.map(o -> o.getId() + ";" + o.getCreationDate())
					.forEach(Unchecked.consumer(writer::write));
			} if (exportType == ExportType.USER) {
				writer.write("Username;FirstName;LastName\n");
				userRepo.findAll().stream()
					.map(u -> u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName())
					.forEach(Unchecked.consumer(writer::write));
			} else {
				throw new IllegalArgumentException();
			}
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}
}

