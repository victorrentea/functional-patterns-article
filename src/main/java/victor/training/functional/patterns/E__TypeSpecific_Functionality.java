package victor.training.functional.patterns;

abstract class Movie {
	public abstract int computePrice(int days);
}

class RegularMovie extends Movie {
	public int computePrice(int days) {
		return days + 1;
	}
}

class NewReleaseMovie extends Movie {
	public int computePrice(int days) {
		return days * 2;
	}
}

class ChildrenMovie extends Movie {
	public int computePrice(int days) {
		return 5;
	}
}

public class E__TypeSpecific_Functionality {
	public static void main(String[] args) {
		System.out.println(new RegularMovie().computePrice(2));
		System.out.println(new NewReleaseMovie().computePrice(2));
		System.out.println(new ChildrenMovie().computePrice(2));
	}
}
