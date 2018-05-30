package victor.training.functional.patterns;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

// get the list of users to UI

class UserFacade {
	
	private UserRepo userRepo;
	
	// @Autowired/@Inject
	private UserMapper userMapper;
	
	public List<UserDto> getAllUsers() {
		return userRepo.findAll().stream().map(userMapper::toDto).collect(toList());
	}

}

// @Component
class UserMapper {
	
	// @Autowired
	private OtherClass otherClass;
	
	public UserDto toDto(User user) {
		UserDto dto = new UserDto();
		dto.setUsername(user.getUsername());
		dto.setFullName(user.getFirstName() + " " + user.getLastName().toUpperCase());
		dto.setActive(user.getDeactivationDate() == null);
		// Code calling a member method of OtherClass
		return dto;
	}
}

// @Component
class OtherClass {
	// stuff
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
}