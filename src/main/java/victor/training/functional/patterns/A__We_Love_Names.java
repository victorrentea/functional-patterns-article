package victor.training.functional.patterns;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

// get the list of users to UI

class UserFacade {
	
	private UserRepo userRepo;
	
	public List<UserDto> getAllUsers() {
		return userRepo.findAll().stream().map(UserDto::new).collect(toList());
	}

}

// VVVVVVVVV ==== supporting (dummy) code ==== VVVVVVVVV
interface UserRepo {
	List<User> findAll();
}

@Data
class User {
	private String firstName;
	private String lastName;
	private String username;
	private LocalDate deactivationDate;
}

@Data
class UserDto {
	private String fullName;
	private String username;
	private boolean active;
	
	public UserDto(User user) {
		username = user.getUsername();
		fullName = user.getFirstName() + " " + user.getLastName().toUpperCase();
		active = user.getDeactivationDate() == null;
	}
}