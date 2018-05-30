package victor.training.functional.patterns;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.extern.slf4j.Slf4j;

// export all orders to a file

interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
@Slf4j
class FileExporter {
	
	public File exportFile(String fileName, Consumer<Writer> contentWriter) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			contentWriter.accept(writer);
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}
}

class ClientCode {
	public static void main(String[] args) throws Exception {
		FileExporter fileExporter = new FileExporter();
		OrderExportWriter orderWriter = new OrderExportWriter();
		UserExportWriter userWriter = new UserExportWriter();
		
		fileExporter.exportFile("orders.csv", Unchecked.consumer(orderWriter::writeOrders));
		fileExporter.exportFile("users.csv", Unchecked.consumer(userWriter::writeUsers));
	}
}

class OrderExportWriter  {
	
	private OrderRepo orderRepo;
	
	public void writeOrders(Writer writer) throws IOException {
		writer.write("OrderID;Date\n");
		orderRepo.findByActiveTrue()
			.map(o -> o.getId() + ";" + o.getCreationDate())
			.forEach(Unchecked.consumer(writer::write));
	}
}

class UserExportWriter {

	private UserRepo userRepo; 
	
	public void writeUsers(Writer writer) throws IOException {
		writer.write("Username;FirstName;LastName\n");
		userRepo.findAll().stream()
			.map(u -> u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName())
			.forEach(Unchecked.consumer(writer::write));
	}
}

@RunWith(MockitoJUnitRunner.class)
class UserExportWriterTest {
	@InjectMocks
	private OrderExportWriter contentWriter;
	
	@Mock
	private OrderRepo repo;
	
	@Test
	public void writesExpectedContent() throws IOException {
		Writer w = new StringWriter();
		when(repo.findByActiveTrue()).thenReturn(Stream.of(new Order(1l, emptyList(), LocalDate.of(2018, 1, 1))));
		contentWriter.writeOrders(w);
		assertEquals("TODO", w.toString());
	}
}