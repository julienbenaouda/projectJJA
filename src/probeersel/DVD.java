package probeersel;

import java.util.ArrayList;
import java.util.List;

public class DVD {
	private List movies=new ArrayList();
	public DVD() {}
	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List movies) {
		this.movies = movies;
	}
	public String toString(){
		String movies="";
		for(Movie movie : getMovies()){
			movies += movie.getRelated() + ", ";
		}
		return movies;
	}
}
