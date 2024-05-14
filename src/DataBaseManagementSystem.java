import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class Movie {

	private String title, overview, originalLanguage, genre, posterUrl;
	private int voteCount;
	private double popularity, voteAverage;
	private LocalDate releaseDate;
	private int[] choices = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	{
		title = "";
		overview = "";
		originalLanguage = "";
		genre = "";
		posterUrl = "";
		popularity = -1;
		voteCount = -1;
		voteAverage = -1;
		releaseDate = LocalDate.of(1, 1, 1);
	}

	public Movie(List<String> entity, int[] choices) {

		if (choices.length < 9)this.choices = choices;
		setReleaseDate(entity.get(0));
		setTitle(entity.get(1));
		setOverview(entity.get(2));
		setPopularity(entity.get(3));
		setVoteCount(entity.get(4));
		setVoteAverage(entity.get(5));
		setOriginalLanguage(entity.get(6));
		setGenre(entity.get(7));
		setPosterUrl(entity.get(8));

	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	private void setOverview(String overview) {
		this.overview = overview;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	private void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public String getGenre() {
		return genre;
	}

	private void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	private void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public double getPopularity() {
		return popularity;
	}

	private void setPopularity(String popularity) {
		double pop = 0;
		try {
			pop = Double.parseDouble(popularity);
			this.popularity = pop;
		} catch (Exception e) {
			System.err.println("Illegal Argument Exception");
		}

	}

	public int getVoteCount() {
		return voteCount;
	}

	private void setVoteCount(String voteCount) {
		int voteCou = 0;
		try {
			voteCou = Integer.parseInt(voteCount);
			this.voteCount = voteCou;
		} catch (Exception e) {
			System.err.println("Illegal Argument Exception");
		}
	}

	public double getVoteAverage() {
		return voteAverage;
	}

	private void setVoteAverage(String voteAverage) {

		double voteAv = 0;
		try {
			voteAv = Double.parseDouble(voteAverage);
			this.voteAverage = voteAv;
		} catch (Exception e) {
			System.err.println("Illegal Argument Exception");
		}

	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	private void setReleaseDate(String releaseDate) {

		String[] date = releaseDate.split("/");
		try {
			int month = Integer.parseInt(date[0]);
			int day = Integer.parseInt(date[1]);
			int year = Integer.parseInt(date[2]);

			this.releaseDate = LocalDate.of(year, month, day);
		} catch (Exception e) {
			System.err.println("Illegal Argument Exception");
		}

	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int choice : this.choices) {
			switch (choice) {
			case 1 -> builder.append(this.releaseDate.toString() + "--");
			case 2 -> builder.append(this.title + "--");
			case 3 -> builder.append(this.overview + "--");
			case 4 -> builder.append(this.popularity + "--");
			case 5 -> builder.append(this.voteCount + "--");
			case 6 -> builder.append(this.voteAverage + "--");
			case 7 -> builder.append(this.originalLanguage + "--");
			case 8 -> builder.append(this.genre + "--");
			case 9 -> builder.append(this.posterUrl);
			}
		}
		return builder.toString();
	}

}
/*********************************************************************************************************************/
class DataBase {

	private Map<Integer, List<String>> dataBase;
	private TreeSet<Integer> choices = new TreeSet<>();
	private int range1, range2;

	{
		range1 = 0;
		range2 = 101;
	}

	public DataBase(String url) {
		dataBase = fetchingData(url);
	}

	public Map<Integer, List<String>> getRangeBasedList(String range) throws Exception {
		setRanges(range);
		return rangeBasedList(this.range1, this.range2);
	}

	public int[] getChoices() {
		if (this.choices.size() == 0) {
			int[] ch = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
			return ch;
		}
		int[] c = new int[this.choices.size()];
		int i = 0;
		for (int choix : this.choices) {
			c[i] = choix;
			i++;
		}

		return c;
	}

	public void setRanges(String range) throws Exception {
		String[] ranges = range.split(",");
		int[] intRanges = new int[2];

		if (ranges.length != 2)
			throw new Exception("Please Enter a correct Interval of range, i.e: 5,100");
		try {
			intRanges[0] = Integer.parseInt(ranges[0]);
			intRanges[1] = Integer.parseInt(ranges[1]);
		} catch (Exception e) {
			throw new Exception("Please Enter a correct Interval of range, i.e: 5,100");
		}
		Arrays.sort(intRanges);
		if (intRanges[0] < 1 || intRanges[0] > dataBase.size() - 1 || intRanges[1] < 1
				|| intRanges[0] > dataBase.size() - 1)
			throw new Exception("Please Enter a correct Interval of range, i.e: 5,100");
		ranges(intRanges[0], intRanges[1]);

	}

	private void ranges(int range1, int range2) {
		this.range1 = range1;
		this.range2 = range2;
	}

	private Map<Integer, List<String>> fetchingData(String url) {

		Map<Integer, List<String>> dataBase = new TreeMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(url))) {
			boolean flag3 = false;
			List<String> reconstructedLines = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			try {

				String line;
				while ((line = br.readLine()) != null) {

					if (line.endsWith("jpg") || line.endsWith("Poster_Url")) {
						if (flag3) {
							builder.append(line);
							reconstructedLines.add(builder.toString());
							builder = new StringBuilder();
						} else {
							reconstructedLines.add(line);
						}
					} else {
						builder.append(line);
						flag3 = true;
					}
				}

				for (int i = 0; i < reconstructedLines.size(); i++) {
					List<String> rowFields = new ArrayList<>();
					rowFields = personalizedSpliter(reconstructedLines.get(i));
					dataBase.put(i, rowFields);
				}

			} catch (Exception e) {

				System.err.println("An unexpected error occurred, please try again.");
			}
		} catch (Exception e) {

			System.err.println(
					"Please make sure your source file resides in your project directory, if it does, please try again.");
		}
		return dataBase;
	}

	private List<String> personalizedSpliter(String line) {
		int index = 0;
		StringBuilder str = new StringBuilder();
		char[] characters = line.toCharArray();
		for (int j = 0; j < characters.length; j++) {
			if (characters[j] == '\"') {
				index++;
			}
			if (index % 2 == 0) {
				if (characters[j] == ',') {
					characters[j] = '<';
				}
			}

			str.append(characters[j]);
		}

		List<String> personalizedList = new ArrayList<>();
		personalizedList = Arrays.asList(str.toString().split("<"));
		return personalizedList;
	}

	public void setChoices(String choices) throws Exception {
		TreeSet<Integer> orderedSetOfChoices = new TreeSet<>(); // using tree set to keep every field in their natural
																// order

		String[] arrayOfChoices = choices.split(",");

		if (arrayOfChoices.length < 1 || arrayOfChoices.length > 9)
			throw new Exception("Please Enter between one (1) and nine (9) choices.");
		for (String entry : arrayOfChoices) {
			int fieldDigit = 0;
			try {
				fieldDigit = Integer.parseInt(entry);
			} catch (Exception e) {
				throw new Exception("Please Enter correct choice(s), integer between 1-9.");
			}
			if (fieldDigit < 1 || fieldDigit > 9)
				throw new Exception("Please Enter correct choice(s), integer between 1-9.");
			orderedSetOfChoices.add(fieldDigit);

		}

		this.choices = orderedSetOfChoices;
		try {
			if (orderedSetOfChoices.size() != arrayOfChoices.length)
				throw new Exception("The duplicate choice(s) has/have been removed.");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public List<String> getListColumns()
	{
		List<String> list=new ArrayList<>();
		
		
		list.add("Release_Date");
		list.add("Title");
		list.add("Overview");
		list.add("Popularity");
		list.add("Vote_Count");
		list.add("Vote_Average");
		list.add("Original_Language");
		list.add("Genre");
		list.add("Poster_Url");
		
		
		return list;
	}

	private Map<Integer, List<String>> rangeBasedList(int range1, int range2) {
		Map<Integer, List<String>> rangeList = new TreeMap<>();

		int index = 0;
		for (int i = range1; i <= range2; i++) {
			rangeList.put(index++, dataBase.get(i));
		}

		return rangeList;
	}

	public Map<Integer, List<String>> getList() {
		int r1 = range1, r2 = range2;
		Map<Integer, List<String>> list = new TreeMap<>();
		for (int i = r1; i < r2; i++) {
			list.put(i, dataBase.get(i));
		}
		return list;
	}
	
	public Map<Integer, List<String>> getDataBase() {
		return dataBase;
	}
	
	public static Set<String> getTotalGenre(List<Movie> movies)
	{
		Set<String> genre=new TreeSet<>();
		for(Movie movie: movies)
		{
			String [] arrayGenre= movie.getGenre().split(",");
			for(String oneGenre: arrayGenre)
			{
				if(oneGenre.contains("\""))
				{
					if(oneGenre.startsWith("\"")) genre.add(oneGenre.substring(1).trim());
					else if(oneGenre.endsWith("\"")) genre.add(oneGenre.substring(0, oneGenre.length()-1).trim());
				}else genre.add(oneGenre.trim());
			}
		}
		return genre;
	}
	
	public static Set<String> getOrignalLang(List<Movie> movies)
	{
		Set<String> languages=new TreeSet<>();
		
		for(Movie movie: movies)
		{
			String lang= movie.getOriginalLanguage();
			
			languages.add(lang);
			
		}
		return languages;
	}
	
}

/**********************************************************************************************************************/
public class DataBaseManagementSystem {

	static DataBase database = new DataBase("mymoviedb.csv");

	public static void main(String[] args) {

		
		menu(); 

	}

	
	
	
	
	
	
	static List<Movie> moviesInstanciationForRange(int[] fieldsChoices, Map<Integer, List<String>> entities) {
		List<Movie> list = new ArrayList<Movie>();

		for (int i = 0; i < entities.size(); i++) {

			Movie movie = new Movie(entities.get(i), fieldsChoices);
			list.add(movie);
		}
		return list;
	}

	static List<Movie> moviesInstanciation(int[] fieldsChoices, Map<Integer, List<String>> entities) {
		List<Movie> list = new ArrayList<Movie>();

		for (int i = 1; i < entities.size(); i++) {

			Movie movie = new Movie(entities.get(i), fieldsChoices);
			list.add(movie);
		}
		return list;
	}

	static void menu() {
		List<Movie> movies = moviesInstanciation(database.getChoices(), database.getList());

		System.out.println(
				"\t\tWelcome to \"Team 22 Data Base Management System\"\t\t\t\n\t\t************************************************\t\t\t\n");
		Scanner scan = new Scanner(System.in);
		boolean flag0 = false;
		do 
		{

			System.out.println("1. List column names\n" + "2. List all entities\n" + "3. Search entities\n"+"4. Filter the entire entites (9827)");
			int choice1 = 0;
			try {
				choice1 = scan.nextInt();
				scan.nextLine();

				// TODO: clearing the buffer
			} catch (Exception e) {
				System.err.println("Please Enter a correct choice");
			}
			switch (choice1) 
			{
				
				case 1 ->
				{
					
					System.out.println("\t1. List column names");
					System.out.println("\t\tFirst column:   Release_Date");
					System.out.println("\t\tSecond column:  Title");
					System.out.println("\t\tThird column:   Overview");
					System.out.println("\t\tFourth column:  Popularity");
					System.out.println("\t\tFifth column:   Vote_Count");
					System.out.println("\t\tSixth column:   Vote_Average");
					System.out.println("\t\tSeventh column: Original_Language");
					System.out.println("\t\tEighth column:  Genre");
					System.out.println("\t\tNineth column:  Poster_Url");
					flag0=false;
				}
				
				case 2 -> 
				{
					
					boolean flag1=false;
					do
					{
						
						System.out.printf("1. List all Entities\n");
						System.out.printf("\ta. List all the fields of each entity\n");
						System.out.printf("\tb. List the selected fields of each entity\n");
						System.out.printf("\tc. List entities based on the range of rows (e.g, range is given; 5-100)\n");
						char choice11 = scan.next().charAt(0);
						scan.nextLine();
						// TODO :clearing the buffer
						switch (choice11) 
						{
							case 'a' -> 
							{
								System.out.printf("\ta. List all the fields of each entity\n");
								movies=moreRequest(movies, scan);
								printAll(movies);
								export(movies, scan);
							}
						
							case 'b' -> 
							{
								boolean flag = false;
								do 
								{
									
									System.out.println("\tb. List the selected fields of each entity");
									System.out.println("\tPlease choose from one (1) to nine (9) number of fields listed below: use comma(,) as separator.");
									System.out.println("\t\t1. Release Date");
									System.out.println("\t\t2. Title");
									System.out.println("\t\t3. Overview");
									System.out.println("\t\t4. Popularity");
									System.out.println("\t\t5. Vote Count");
									System.out.println("\t\t6. Vote Average");
									System.out.println("\t\t7. Original Language");
									System.out.println("\t\t8. Genre");
									System.out.println("\t\t9. Poster Url");
									System.out.println("\t\tN.B: The obtained list can even be sorted based on unselected field!");
									
									String fieldsChoice = scan.nextLine();
									fieldsChoice = trimming(fieldsChoice);
									try {
										database.setChoices(fieldsChoice);
										flag = false;
									} catch (Exception e) {
										flag = true;
										System.err.println(e.getMessage());
									}
								} while (flag);
								movies = moviesInstanciation(database.getChoices(), database.getList());
								moreRequest(movies, scan);
								printAll(movies);
								export(movies, scan);
								
							}
						
							case 'c' ->
							{
								boolean flag = false;
								do 
								{
									System.out.println("\tc. List entities based on the range of rows (e.g, range is given; 5,100)");
									System.out.println("\t\tPlease Enter two (2) numbers between 1-9828 separated by comma (,)");
									System.out.print("\t\tWaiting for your range interval: ");
									
									String range = scan.nextLine();
									range = trimming(range);
									try {
										movies = moviesInstanciationForRange(database.getChoices(),
												database.getRangeBasedList(range));
										flag = false;
									} catch (Exception e) {
										flag = true;
										System.err.println("\n" + e.getMessage());
									}
								} while (flag);
								moreRequest(movies, scan);
								printAll(movies);
								export(movies, scan);
								
							}
						
							default -> 
							{
								System.err.println("Please Enter a correct choice");
								flag1 = true;
							}
						}
					}while(flag1);
					flag0=false;
	
				}
				
				case 3 ->
				{
					movies = moviesInstanciation(database.getChoices(), database.getDataBase());
					movies=search(movies, scan);
					
					if(movies.size()!=0)
					{
						moreRequest(movies, scan);
						printAll(movies);
						export(movies, scan);
					}
					if(movies.size()==0) System.out.println("\tNo result has been found.");
					flag0=false;
				}
				case 4 ->
				{
					System.out.println("\t4. Filter/Sort the entire entities (9827).");
					List<Movie> entireMovies= moviesInstanciation(new int[]{1,2,3,4,5,6,7,8,9},database.getDataBase());
					entireMovies=filter(entireMovies, scan);
					entireMovies=moreRequest(entireMovies,scan);
					printAll(entireMovies);
					export(entireMovies, scan);
				}
				
				default ->
				{
					System.err.println("Please Enter a correct choice");
					flag0=true;
				}

			}
			boolean flag01= false;
			do
			{
				
				System.out.println("\nWould you like to start a new Operation?");
				System.out.println("Please Enter (y) for yes or (n) for no: ");
				if(scan.hasNextLine() && choice1!=1) scan.nextLine();
				try {
						char restart=scan.nextLine().toLowerCase().charAt(0);
						switch(restart)
						{
						case 'y' -> flag0=true;
						case 'n' -> flag0=false;
						default ->
						{
							System.err.println("Please Enter a correct choice");
							flag01=true;
						}
					}
				}catch(Exception e){
					System.err.println("You BRUTALLY terminated the program.");
					flag0=false;
					
				}
			}while(flag01);
		} while (flag0);

		scan.close();
	}

	static void sortingChoice(List<Movie> movies, Scanner scan) {

		boolean bigFlag = false;
		do {
			System.out.println("\t\tSort the entities");
			System.out.println("\t\t\ta. Based on any field: In Ascending order");
			System.out.println("\t\t\tb. Based on any field: In Descending order");
			char choice12 = scan.nextLine().toLowerCase().charAt(0);
			switch (choice12) {
			case 'a' -> {
				int fieldNumber = 0;
				boolean flag = false;
				do {
					System.out.println("\t\t\ta. Sorting Based on a choosen field: In Ascending order");
					System.out.println("\t\t\tPlease choose one (1) field among the below list of fields for sorting (1-9)");
					System.out.println("\t\t\t1. Release Date");
					System.out.println("\t\t\t2. Title");
					System.out.println("\t\t\t3. Overview");
					System.out.println("\t\t\t4. Popularity");
					System.out.println("\t\t\t5. Vote Count");
					System.out.println("\t\t\t6. Vote Average");
					System.out.println("\t\t\t7. Original Language");
					System.out.println("\t\t\t8. Genre");
					System.out.println("\t\t\t9. Poster Url");

					try {
						fieldNumber = scan.nextInt();
						if (fieldNumber < 1 || fieldNumber > 9)
							throw new Exception();
						flag = false;
					} catch (Exception e) {
						flag = true;
						scan.nextLine();
						System.err.println("Please Enter a correct choice");
					}
				} while (flag);
				sort(movies, fieldNumber, 0);
				scan.nextLine();
				bigFlag = false;
			}

			case 'b' -> {
				int fieldNumber = 0;
				boolean flag = false;
				do {

					System.out.println("\t\t\ta. Sorting Based on a choosen field: In Descending order");
					System.out.println("\t\t\tPlease choose one (1) field among the below list of fields (1-9)");
					System.out.println("\t\t\t1. Release Date");
					System.out.println("\t\t\t2. Title");
					System.out.println("\t\t\t3. Overview");
					System.out.println("\t\t\t4. Popularity");
					System.out.println("\t\t\t5. Vote Count");
					System.out.println("\t\t\t6. Vote Average");
					System.out.println("\t\t\t7. Original Language");
					System.out.println("\t\t\t8. Genre");
					System.out.println("\t\t\t9. Poster Url");

					try {
						fieldNumber = scan.nextInt();
						if (fieldNumber < 1 || fieldNumber > 9)
							throw new Exception();
						flag = false;
					} catch (Exception e) {
						flag = true;
						scan.nextLine();
						System.err.println("Please Enter a correct choice");
					}
				} while (flag);
				scan.nextLine();
				sort(movies, fieldNumber, 1);
				bigFlag = false;
			}
			default -> {
				System.err.println("Please Enter a correct choice");
				bigFlag = true;
			}
			}
		} while (bigFlag);
	}

	static List<Movie> moreRequest(List<Movie> movies, Scanner scan) {
		boolean flagChoice2 = false;
		do {

			System.out.print(
					"\t\tTo Sort/Filter the entities\n\t\tPlease Enter (Y) for yes or (N) for no: ");
			char choice2 = scan.nextLine().toLowerCase().charAt(0);
			switch (choice2) {

			case 'y' -> {
				boolean flagChoice3 = false;
				do {

					System.out.println("\t\tPlease choose between (a), (b), or (c)");
					System.out.println("\t\ta. Sort the entities");
					System.out.println("\t\tb. Filter the entities");
					System.out.println("\t\tc. Filter and Sort the entities");
					char choice3 = scan.nextLine().toLowerCase().charAt(0);
					switch (choice3) {
					case 'a' -> {
						sortingChoice(movies, scan);
					}

					case 'b' -> {
						movies=filter(movies, scan);
						System.out.println("****second size"+movies.size());
					}
					case 'c' ->
					{
						movies=filter(movies, scan);
						sortingChoice(movies, scan);
						System.out.println("\t\tThe entites have been filtered and sorted successfully!"); //TODO: filter
					}
					default -> {

						System.err.println("Please Enter correct choice: (a) or (b)");
						flagChoice3 = true;

					}
					}
				} while (flagChoice3);

			}
			case 'n' -> {
				flagChoice2 = false;
			}
			default -> {
				System.err.println("Please Enter correct choice: y/n or Y/N");
				flagChoice2 = true;
			}

			}
		} while (flagChoice2);
		return movies;
	}

	static void sort(List<Movie> movies, int fieldNumber, int order) {
		switch (fieldNumber) {
		case 1 -> {
			if (order == 0) {
				Comparator<Movie> releaseDateComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getReleaseDate().compareTo(m2.getReleaseDate());
					}
				};
				Collections.sort(movies, releaseDateComparatorASC);
			} else {
				Comparator<Movie> releaseDateComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getReleaseDate().compareTo(m1.getReleaseDate());
					}
				};
				Collections.sort(movies, releaseDateComparatorDSC);
			}
		}

		case 2 -> {
			if (order == 0) {
				Comparator<Movie> titleComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getTitle().compareTo(m2.getTitle());
					}
				};
				Collections.sort(movies, titleComparatorASC);
			} else {
				Comparator<Movie> titleComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getTitle().compareTo(m1.getTitle());
					}
				};
				Collections.sort(movies, titleComparatorDSC);
			}
		}

		case 3 -> {
			if (order == 0) {
				Comparator<Movie> overviewComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getOverview().compareTo(m2.getOverview());
					}
				};
				Collections.sort(movies, overviewComparatorASC);
			} else {
				Comparator<Movie> overviewComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getOverview().compareTo(m1.getOverview());
					}
				};
				Collections.sort(movies, overviewComparatorDSC);
			}
		}

		case 4 -> {
			if (order == 0) {
				Comparator<Movie> popularityComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return ((m1.getPopularity() - m2.getPopularity()) > 0 ? 1
								: ((m1.getPopularity() - m2.getPopularity()) < 0) ? -1 : 0);
					}
				};
				Collections.sort(movies, popularityComparatorASC);
			} else {
				Comparator<Movie> popularityComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return (m2.getPopularity() - m1.getPopularity()) > 0 ? 1
								: ((m2.getPopularity() - m1.getPopularity()) < 0) ? -1 : 0;
					}
				};
				Collections.sort(movies, popularityComparatorDSC);
			}
		}

		case 5 -> {
			if (order == 0) {
				Comparator<Movie> voteCountComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getVoteCount() - m2.getVoteCount();
					}
				};
				Collections.sort(movies, voteCountComparatorASC);
			} else {
				Comparator<Movie> voteCountComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getVoteCount() - m1.getVoteCount();
					}
				};
				Collections.sort(movies, voteCountComparatorDSC);
			}
		}

		case 6 -> {
			if (order == 0) {
				Comparator<Movie> voteAverageComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return (m1.getVoteAverage() - m2.getVoteAverage()) > 0 ? 1
								: ((m1.getVoteAverage() - m2.getVoteAverage()) < 0) ? -1 : 0;
					}
				};
				Collections.sort(movies, voteAverageComparatorASC);
			} else {
				Comparator<Movie> voteAverageComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return (m2.getVoteAverage() - m1.getVoteAverage()) > 0 ? 1
								: ((m2.getVoteAverage() - m1.getVoteAverage()) < 0) ? -1 : 0;
					}
				};
				Collections.sort(movies, voteAverageComparatorDSC);
			}
		}

		case 7 -> {
			if (order == 0) {
				Comparator<Movie> originalLangComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getOriginalLanguage().compareTo(m2.getOriginalLanguage());
					}
				};
				Collections.sort(movies, originalLangComparatorASC);
			} else {
				Comparator<Movie> originalLangComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getOriginalLanguage().compareTo(m1.getOriginalLanguage());
					}
				};
				Collections.sort(movies, originalLangComparatorDSC);
			}
		}

		case 8 -> {
			if (order == 0) {
				Comparator<Movie> genreComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getGenre().compareTo(m2.getGenre());
					}
				};
				Collections.sort(movies, genreComparatorASC);
			} else {
				Comparator<Movie> genreComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getGenre().compareTo(m1.getGenre());
					}
				};
				Collections.sort(movies, genreComparatorDSC);
			}
		}

		case 9 -> {
			if (order == 0) {
				Comparator<Movie> posterUrlComparatorASC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m1.getPosterUrl().compareTo(m2.getPosterUrl());
					}
				};
				Collections.sort(movies, posterUrlComparatorASC);
			} else {
				Comparator<Movie> posterUrlComparatorDSC = new Comparator<Movie>() {
					public int compare(Movie m1, Movie m2) {
						return m2.getPosterUrl().compareTo(m1.getPosterUrl());
					}
				};
				Collections.sort(movies, posterUrlComparatorDSC);
			}
		}
		}

	}

	static String trimming(String string) {
		String[] w = string.split(" ");
		StringBuilder strb = new StringBuilder();
		for (String str : w) {
			strb.append(str);
		}
		return strb.toString();
	}

	static void printAll(List<Movie> movies) {

		movies.forEach(movie -> System.out.println("-->" + movie));
		System.out.println("Number of Entities Listed: " + movies.size());
	}

	static List<Movie> search(List<Movie> movies,Scanner scan)
	{
		
		List<Movie> foundList= new ArrayList<>();
		boolean searchFlag=false;
		do
		{
			
			System.out.println("3. Search entities");
			System.out.println("\tBased on which field would you like to search the entities?");
			System.out.println("\tPlease select one (1) field among the below list of fields by typing e.g: a");
			System.out.println("\t\t\ta. Release Date");
			System.out.println("\t\t\tb. Title");
			System.out.println("\t\t\tc. Overview");
			System.out.println("\t\t\td. Popularity");
			System.out.println("\t\t\te. Vote Count");
			System.out.println("\t\t\tf. Vote Average");
			System.out.println("\t\t\tg. Original Language");
			System.out.println("\t\t\th. Genre");
			System.out.println("\t\t\ti. Poster Url");
			char alph=scan.nextLine().toLowerCase().charAt(0);

			switch(alph)
			{
				case 'a'->
				{
					boolean flagDate=false;
					do
					{
						
						System.out.print("\tPlease Enter your date in this format: YYYY-MM-DD: 1999-12-31. "
								+ "\n\tPlease insert 0 before single digit of Month and Day, i.e: 1999-03-05\n\t");
						String date=scan.nextLine();
						if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
						{
							flagDate=true;
							System.err.println("\tPlease Enter a correct date format as asked.");
						}
						else flagDate=false;
						if(!flagDate) foundList=movies.stream().filter(movie->movie.getReleaseDate().toString().equals(date)).collect(Collectors.toList());
					}while(flagDate);
					
				}
				case 'b'->
				{
					System.out.print("\tPlease Enter your title: ");
					String title=scan.nextLine().toLowerCase();
					foundList=movies.stream().filter(movie->movie.getTitle().toLowerCase().contains(title)).collect(Collectors.toList());
				}
				case 'c'->
				{
					System.out.print("\tPlease Enter your Overview: \n\t\t");
					String overview=scan.nextLine().toLowerCase();
					foundList=movies.stream().filter(movie->movie.getOverview().toLowerCase().contains(overview)).collect(Collectors.toList());
				}
				case 'd'->
				{
					boolean flagpop=false;
					do
					{
						System.out.print("\tPlease Enter your popularity: ");
						try
						{
							double pop=scan.nextDouble();
							foundList=movies.stream().filter(movie->(movie.getPopularity()==pop)).collect(Collectors.toList());
							flagpop=false;
							scan.nextLine();
						}catch(Exception e)
						{
							System.err.println("Please Enter popularity as number.");
							scan.nextLine();
							flagpop=true;
						}
					}while(flagpop);
				}
				case 'e'->
				{
					boolean flagVoteCount=false;
					do
					{
						System.out.print("\tPlease Enter your vote count: ");
						try
						{
							double voteCount=scan.nextDouble();
							foundList=movies.stream().filter(movie->(movie.getVoteCount()==voteCount)).collect(Collectors.toList());
							flagVoteCount=false;
							scan.nextLine();
						}catch(Exception e)
						{
							System.err.println("Please Enter vote count as number.");
							scan.nextLine();
							flagVoteCount=true;
						}
					}while(flagVoteCount);
				}
				case 'f'->
				{
					boolean flagVoteAv=false;
					do
					{
						System.out.print("\tPlease Enter your vote average: ");
						try
						{
							double voteAv=scan.nextDouble();
							foundList=movies.stream().filter(movie->(movie.getVoteAverage()==voteAv)).collect(Collectors.toList());
							flagVoteAv=false;
							scan.nextLine();
						}catch(Exception e)
						{
							System.err.println("Please Enter vote average as number.");
							scan.nextLine();
							flagVoteAv=true;
						}
					}while(flagVoteAv);
				}
				case 'g'->
				{
					System.out.print("\tPlease Enter your original language: ");
					String lang=scan.nextLine().toLowerCase();
					//foundList=movies.stream().filter(movie->movie.getTitle().toLowerCase().contains(lang)).collect(Collectors.toList());
					foundList= language(movies, foundList, lang);
				}
				case 'h'->
				{
					
					Set<String> genres=DataBase.getTotalGenre(movies);
					System.out.printf("\tHere is the list of genres (%d):\n", genres.size());
					genres.forEach(genre->System.out.println("\t\t"+genre));
					System.out.print("\tPlease Enter your choosen genre: ");
					String genre=scan.nextLine().toLowerCase();
					foundList=movies.stream().filter(movie->movie.getGenre().toLowerCase().contains(genre)).collect(Collectors.toList());
				}
				case 'i'->
				{
					System.out.print("\tPlease Enter your poster URL: ");
					String lang=scan.nextLine().toLowerCase();
					foundList=movies.stream().filter(movie->movie.getOriginalLanguage().contains(lang)).collect(Collectors.toList());
				}
				default ->
				{
					System.err.println("Please select a correct field as required.");
					searchFlag=true;
				}
			}
		}while(searchFlag);
		
		
		return foundList;
	}
	
	static List<Movie> filter(List<Movie> movies,Scanner scan)
	{//TODO: redundant choice
		
			List<Movie> filteredList= new ArrayList<>();
	
		
			
			System.out.println("\tFilter entities");
			System.out.println("\tPlease select one (1) or set of fields on which you want to filter your entites: use comma(,) as separator.");
			System.out.println("\t\t1. Release Date");
			System.out.println("\t\t2. Title");
			System.out.println("\t\t3. Overview");
			System.out.println("\t\t4. Popularity");
			System.out.println("\t\t5. Vote Count");
			System.out.println("\t\t6. Vote Average");
			System.out.println("\t\t7. Original Language");
			System.out.println("\t\t8. Genre");
			System.out.println("\t\t9. Poster Url");
			System.out.print("\tselection: ");
			
			String selectedFields=scan.nextLine();
			selectedFields = trimming(selectedFields);
			String [] filterFields=selectedFields.split(",");
			Set<String> setFields=new LinkedHashSet<>();
			setFields.addAll(Arrays.asList(filterFields));
			
			
			if(setFields.size()!=filterFields.length)
			{
				System.err.println("\tThe duplicate choices have been removed.");
			}
			
			
			
			for(String field: setFields)
			{
				switch(field)
				{
				case "1"->
				{
					boolean flag1=false;
					do
					{
						
						System.out.println("\tI. Release Date");
						System.out.println("\t\t1. Equal");
						System.out.println("\t\t2. Greater than");
						System.out.println("\t\t3. Less than");
						System.out.println("\t\t4. Greater and Equal to");
						System.out.println("\t\t5. Less and equal to");
						System.out.println("\t\t6. Between");
						System.out.println("\t\t7. Missing Date");
						System.out.println("\t\t8. In a specific year");
						System.out.println("\t\t9. In a specific month");
						System.out.println("\t\t10.In a specific day");
						System.out.print("\tselection: ");
						String operation=scan.nextLine();
						switch(operation)
						{
						case "1"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t1. Equal");
								System.out.print("\tPlease Enter your in this format, 1999-03-05: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().equals(date)).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
							
						}
						case "2"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t2. Greater than");
								System.out.print("\tPlease Enter your in this format, 1999-03-05: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().compareTo(date)>0).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "3"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t3. less than");
								System.out.print("\tPlease Enter your in this format, 1999-03-05: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().compareTo(date)<0).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "4"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t4. Greater and Equal to");
								System.out.print("\tPlease Enter your in this format, 1999-03-05: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().compareTo(date)>=0).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "5"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t5. Less and Equal to");
								System.out.print("\tPlease Enter your in this format, 1999-03-05: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().compareTo(date)<=0).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "6"->
						{
							boolean flagDate=false;
							do
							{
								
								System.out.println("\t6. Between, i.e: 1999-05-03,1999-05-30");
								System.out.print("\tPlease Enter your date interval in this format, 1999-05-03,1999-05-30, separated by comma (,): ");
								String []date=scan.nextLine().split(",");
								if(!date[0].matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}") || !date[1].matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}") || date.length!=2)
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								Arrays.sort(date);
								if(!flagDate) filteredList= movies.stream()
										.filter(movie->{
											
											return (movie.getReleaseDate().toString().compareTo(date[0])>=0 && movie.getReleaseDate().toString().compareTo(date[1])<=0);
										})
										.collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "7"->
						{
							filteredList= movies.stream().filter(movie->movie.getReleaseDate().equals(LocalDate.of(1, 1, 1))).collect(Collectors.toList());
							System.out.println("\t7. Missing date: Filtered!");
							flag1=false;
							
						}
						case "8"->
						{
							boolean flagDate=false;
							do
							{
								System.out.println("\t8. In a specific year");
								System.out.print("\tPlease Enter your year. i.e, 1999: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{4}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct date format as asked.");
								}
								if(!flagDate) filteredList= movies.stream().filter(movie->movie.getReleaseDate().toString().contains(date)).collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "9"->
						{
							boolean flagDate=false;
							do
							{
								System.out.println("\t9. In a specific month");
								System.out.print("\tPlease Enter your month. i.e, 05 or 12: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct month format as asked.");
								}
								if(!flagDate) filteredList= movies.stream()
										.filter(movie->movie.getReleaseDate().toString().subSequence(5, 7).toString().contains(date))
										.collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						case "10"->
						{
							boolean flagDate=false;
							do
							{
								System.out.println("\t10. In a specific day");
								System.out.print("\tPlease Enter your day. i.e, 05 or 12: ");
								String date=scan.nextLine();
								if(!date.matches("[0-9]{2}"))
								{
									flagDate=true;
									System.err.println("\tPlease Enter a correct month format as asked.");
								}
								if(!flagDate) filteredList= movies.stream()
										.filter(movie->movie.getReleaseDate().toString().subSequence(8, 10).toString().contains(date))
										.collect(Collectors.toList());
							}while(flagDate);
							flag1=false;
						}
						default ->
						{
							System.err.println("Pleaser select a correct operation as listed.");
							flag1=true;
						}
						}
					}while(flag1);
					
				}
				case "2"->
				{
					boolean flag2=false;
					do
					{
						System.out.println("\tII. Title"); // TODO:  testing
						System.out.println("\t\t1. Starts With");
						System.out.println("\t\t2. Ends With");
						System.out.println("\t\t3. Contains");
						System.out.print("\tSelection: ");
						String choice=scan.nextLine();
						switch(choice)
						{
						case "1"->
						{
							System.out.println("\t1. Starts With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							filteredList= movies.stream()
									.filter(movie -> movie.getTitle().toLowerCase().startsWith(word) || movie.getTitle().toLowerCase().startsWith("\""+word) || movie.getTitle().toLowerCase().startsWith("\"\""+word) )
									.collect(Collectors.toList());
							
							flag2=false;
						}
						case "2"->
						{
							System.out.println("\t2. Ends With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getTitle().toLowerCase().endsWith(word) || movie.getTitle().toLowerCase().endsWith(word+"\"") || movie.getTitle().toLowerCase().endsWith(word+"\"\"") )
									.collect(Collectors.toList());
							flag2=false;
						}
						case "3"->
						{
							System.out.println("\t3. Contains");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getTitle().toLowerCase().contains(word))
									.collect(Collectors.toList());
							flag2=false;
							
						}
						default ->
						{
							
							System.err.println("Please select a correct operation.");
							flag2=true;
						}
						}
					}while(flag2);
					
					
				}
				case "3"->
				{
					boolean flag2=false;
					do
					{
						System.out.println("\tIII. Overview"); 
						System.out.println("\t\t1. Starts With");
						System.out.println("\t\t2. Ends With");
						System.out.println("\t\t3. Contains");
						System.out.print("\tSelection: ");
						String choice=scan.nextLine();
						switch(choice)
						{
						case "1"->
						{
							System.out.println("\t1. Starts With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							filteredList= movies.stream()
									.filter(movie -> movie.getOverview().toLowerCase().startsWith(word) || movie.getOverview().toLowerCase().startsWith("\""+word) || movie.getTitle().toLowerCase().startsWith("\"\""+word) )
									.collect(Collectors.toList());
							
							flag2=false;
						}
						case "2"->
						{
							System.out.println("\t2. Ends With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getOverview().toLowerCase().endsWith(word) || movie.getOverview().toLowerCase().endsWith(word+"\"") || movie.getTitle().toLowerCase().endsWith(word+"\"\"") )
									.collect(Collectors.toList());
							flag2=false;
						}
						case "3"->
						{
							System.out.println("\t3. Contains");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getOverview().toLowerCase().contains(word))
									.collect(Collectors.toList());
							flag2=false;
							
						}
						default ->
						{
							
							System.err.println("Please select a correct operation.");
							flag2=true;
						}
						}
					}while(flag2);
					
				}
				case "4"->
				{
					boolean flag1=false;
					do
					{
						
							System.out.println("\tIV. Popularity");
							System.out.println("\t\t1. Equal");
							System.out.println("\t\t2. Greater than");
							System.out.println("\t\t3. Less than");
							System.out.println("\t\t4. Greater and Equal to");
							System.out.println("\t\t5. Less and equal to");
							System.out.println("\t\t6. Between");
							System.out.println("\t\t7. Missing Popularity");
							System.out.print("\tselection: ");
							String operation=scan.nextLine();
							switch(operation)
							{
								case "1"->
								{
									
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t1. Equal");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()==pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
									
								}
								case "2"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t2. Greater than");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()>pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "3"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t3. Less than");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()<pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "4"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t4. Greater and Equal to");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()>=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "5"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t5. Less and Equal to");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()<=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "6"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t6. Between, i.e: 100,200");
										System.out.print("\tPlease Enter your popularity interval separated by comma (,): ");
										String popularity=scan.nextLine();
										popularity=trimming(popularity);
										String popArray[]=popularity.split(",");
										try
										{
											if(popArray.length!=2) throw new Exception();
											final Double arrayPop[]= new Double[] {Double.parseDouble(popArray[0]),Double.parseDouble(popArray[1])};
											Arrays.sort(arrayPop);
											if(arrayPop[0]<0 && arrayPop[1]<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getPopularity()>=arrayPop[0] || movie.getPopularity()<=arrayPop[1]).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity interval in the same format resuired as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "7"->
								{
									filteredList= movies.stream().filter(movie->movie.getPopularity()==-1).collect(Collectors.toList());
									System.out.println("\t7. Missing popularity: Filtered!");
									flag1=false;
									
								}
								default ->
								{
									System.err.println("Please Enter a correct opration to be perfomred.");
								}
						}
					}while(flag1);
				}
				case "5"->
				{
					boolean flag1=false;
					do
					{
						
							System.out.println("\tV. Vote Count");
							System.out.println("\t\t1. Equal");
							System.out.println("\t\t2. Greater than");
							System.out.println("\t\t3. Less than");
							System.out.println("\t\t4. Greater and Equal to");
							System.out.println("\t\t5. Less and equal to");
							System.out.println("\t\t6. Between");
							System.out.println("\t\t7. Missing Popularity");
							System.out.print("\tselection: ");
							String operation=scan.nextLine();
							switch(operation)
							{
								case "1"->
								{
									
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t1. Equal");
										System.out.print("\tPlease Enter your vote count as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final int pop=Integer.parseInt(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()==pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote count as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
									
								}
								case "2"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t2. Greater than");
										System.out.print("\tPlease Enter your vote count as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final int pop=Integer.parseInt(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()>pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote count as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "3"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t3. Less than");
										System.out.print("\tPlease Enter your vote count as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final int pop=Integer.parseInt(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()<pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote count as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "4"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t4. Greater and Equal to");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final int pop=Integer.parseInt(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()>=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote count as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "5"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t5. Less and Equal to");
										System.out.print("\tPlease Enter your popularity as a number: ");
										String popularity=scan.nextLine();
										try
										{
											final int pop=Integer.parseInt(popularity);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()<=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote count as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "6"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t6. Between, i.e: 100,200");
										System.out.print("\tPlease Enter your vote count interval separated by comma (,): ");
										String popularity=scan.nextLine();
										popularity=trimming(popularity);
										String popArray[]=popularity.split(",");
										try
										{
											if(popArray.length!=2) throw new Exception();
											final Integer arrayPop[]= new Integer[] {Integer.parseInt(popArray[0]),Integer.parseInt(popArray[1])};
											Arrays.sort(arrayPop);
											if(arrayPop[0]<0 && arrayPop[1]<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteCount()>=arrayPop[0] || movie.getVoteCount()<=arrayPop[1]).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter popularity interval in the same format resuired as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "7"->
								{
									filteredList= movies.stream().filter(movie->movie.getVoteCount()==-1).collect(Collectors.toList());
									System.out.println("\t7. Missing vote count: Filtered!");
									flag1=false;
									
								}
								default ->
								{
									System.err.println("Please Enter a correct opration to be perfomred.");
								}
						}
					}while(flag1);
				}
				case "6"->
				{
					boolean flag1=false;
					do
					{
						
							System.out.println("\tIV. Vote Average");
							System.out.println("\t\t1. Equal");
							System.out.println("\t\t2. Greater than");
							System.out.println("\t\t3. Less than");
							System.out.println("\t\t4. Greater and Equal to");
							System.out.println("\t\t5. Less and equal to");
							System.out.println("\t\t6. Between");
							System.out.println("\t\t7. Missing Popularity");
							System.out.print("\tselection: ");
							String operation=scan.nextLine();
							switch(operation)
							{
								case "1"->
								{
									
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t1. Equal");
										System.out.print("\tPlease Enter your vote average as a number: ");
										String va=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(va);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()==pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
									
								}
								case "2"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t2. Greater than");
										System.out.print("\tPlease Enter your vote average as a number: ");
										String va=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(va);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()>pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "3"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t3. Less than");
										System.out.print("\tPlease Enter your vote average as a number: ");
										String va=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(va);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()<pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "4"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t4. Greater and Equal to");
										System.out.print("\tPlease Enter your vote average as a number: ");
										String va=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(va);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()>=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "5"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t5. Less and Equal to");
										System.out.print("\tPlease Enter your vote average as a number: ");
										String va=scan.nextLine();
										try
										{
											final double pop=Double.parseDouble(va);
											if(pop<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()<=pop).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "6"->
								{
									boolean flagPop=false;
									do
									{
										
										System.out.println("\t6. Between, i.e: 100,200");
										System.out.print("\tPlease Enter your vote average interval separated by comma (,): ");
										String va=scan.nextLine();
										va=trimming(va);
										String popArray[]=va.split(",");
										try
										{
											if(popArray.length!=2) throw new Exception();
											final Double arrayPop[]= new Double[] {Double.parseDouble(popArray[0]),Double.parseDouble(popArray[1])};
											Arrays.sort(arrayPop);
											if(arrayPop[0]<0 && arrayPop[1]<0) throw new Exception();
											filteredList= movies.stream().filter(movie->movie.getVoteAverage()>=arrayPop[0] || movie.getVoteAverage()<=arrayPop[1]).collect(Collectors.toList());
											flagPop=false;
										}catch(Exception e)
										{
											System.err.println("Please Enter vote average interval in the same format resuired as positive number.");
											flagPop=true;
										}
									}while(flagPop);
									flag1=false;
								}
								case "7"->
								{
									filteredList= movies.stream().filter(movie->movie.getVoteAverage()==-1).collect(Collectors.toList());
									System.out.println("\t7. Missing vote average: Filtered!");
									flag1=false;
									
								}
								default ->
								{
									System.err.println("Please Enter a correct opration to be perfomred.");
								}
						}
					}while(flag1);
				}
				case "7"->
				{
					//TODO: if not list found
					
					System.out.println("\tVII. Original Language"); 
					System.out.print("\t\tPlease Enter your choice of language: ");
					String choice=scan.nextLine().toLowerCase();		
					filteredList = language(movies, filteredList, choice);
					
					
					
					
				}
				case "8"->
				{
					System.out.println("\tVIII. Genre"); 
					System.out.print("\t\tPlease Enter your choice(s) of genre, if multiple, separate by comma(,): ");
					String choice=scan.nextLine().toLowerCase();
					choice=trimming(choice);
					String []arrayChoices=choice.split(",");
					Set<Movie> list=new LinkedHashSet<>();
					for(String ch: arrayChoices)
					{
						
						filteredList= movies.stream()
								.filter(movie -> movie.getGenre().toLowerCase().contains(ch))
								.collect(Collectors.toList());
						list.addAll(filteredList);
					}
					
					filteredList= new ArrayList<>(list);
					
				}
				case "9"->
				{
					boolean flag2=false;
					do
					{
						System.out.println("\tIX. Poster Url"); 
						System.out.println("\t\t1. Starts With");
						System.out.println("\t\t2. Ends With");
						System.out.println("\t\t3. Contains");
						System.out.print("\tSelection: ");
						String choice=scan.nextLine();
						switch(choice)
						{
						case "1"->
						{
							System.out.println("\t1. Starts With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							filteredList= movies.stream()
									.filter(movie -> movie.getPosterUrl().toLowerCase().startsWith(word) || movie.getPosterUrl().toLowerCase().startsWith("\""+word) || movie.getPosterUrl().toLowerCase().startsWith("\"\""+word) )
									.collect(Collectors.toList());
							
							flag2=false;
						}
						case "2"->
						{
							System.out.println("\t2. Ends With");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getPosterUrl().toLowerCase().endsWith(word) || movie.getPosterUrl().toLowerCase().endsWith(word+"\"") || movie.getPosterUrl().toLowerCase().endsWith(word+"\"\"") )
									.collect(Collectors.toList());
							flag2=false;
						}
						case "3"->
						{
							System.out.println("\t3. Contains");
							System.out.println("\tPlease Enter your word: ");
							String word=scan.nextLine().toLowerCase();
							movies= movies.stream()
									.filter(movie -> movie.getPosterUrl().toLowerCase().contains(word))
									.collect(Collectors.toList());
							flag2=false;
							
						}
						default ->
						{
							
							System.err.println("Please select a correct operation.");
							flag2=true;
						}
						}
					}while(flag2);
				}
				default ->
				{
					System.err.println("The wrong choice has not been processed.");
				}
				
			}
		
			}
	
		return filteredList;
	}







	static List<Movie> language(List<Movie> movies, List<Movie> filteredList, String choice) {
		switch(choice)
		{
		
		case "english"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("en"))
					.collect(Collectors.toList());
		}
		case "spanish"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("es"))
					.collect(Collectors.toList());
		}
		case "french"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("fr"))
					.collect(Collectors.toList());
		}
		case "italian"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("it"))
					.collect(Collectors.toList());
		}
		case "indian"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("id"))
					.collect(Collectors.toList());
		}
		case "japanese"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("ja"))
					.collect(Collectors.toList());
		}
		case "korean"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("ko"))
					.collect(Collectors.toList());
		}
		case "portuguese"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("pt"))
					.collect(Collectors.toList());
		}
		case "russian"->
		{
			filteredList= movies.stream()
					.filter(movie -> movie.getOriginalLanguage().toLowerCase().equals("ru"))
					.collect(Collectors.toList());
		}
		default ->
		{
			System.out.println("\tNot match has been found for the given language.");
		}
		}
		return filteredList;
	}

	static void export(List<Movie> movies, Scanner scan)
	{
		
		boolean flag=false;
		do
		{
			
			System.out.println("\n\tWould you like to Export your result(s) in a CSV file, will be residing in project folder?");
			System.out.print("\tPlease Enter y for yes or n for no: ");
			
			String fileName=LocalDateTime.now().toString();
			String nameChunks[]=fileName.split(":");
			StringBuilder sb=new StringBuilder();
			for(int i=0; i<nameChunks.length; i++)
			{
				sb.append(nameChunks[i]);
				if(i<nameChunks.length-1) sb.append("-");
			}
			fileName=sb.toString();
			
			String decision=scan.nextLine().toLowerCase();
			Path path=Paths.get(fileName+".csv");
			try{
				Files.createFile(path);
			}catch(Exception e) {}
			List<String> col= database.getListColumns();
			switch(decision)
			{
			case "y"->
			{
				try(FileWriter fw= new FileWriter(new File(fileName+".csv"), true))
				{
					for(int i=0; i<col.size(); i++)
					{
						fw.write(col.get(i));
						if(i<col.size()-1) fw.write(",");
					}
					fw.write('\n');
					for(Movie movie: movies)
					{
						fw.write(movie.getReleaseDate().toString()+",");
						fw.write(movie.getTitle()+",");
						fw.write(movie.getOverview()+",");
						fw.write(String.valueOf(movie.getPopularity())+",");
						fw.write(movie.getVoteCount()+",");
						fw.write(String.valueOf(movie.getVoteAverage())+",");
						fw.write(movie.getOriginalLanguage()+",");
						fw.write(movie.getGenre()+",");
						fw.write(movie.getPosterUrl());
						fw.write('\n');
					}
					
					System.out.println("\tYour file has been saved as, "+fileName);
					System.out.println("");
				}catch(Exception e)
				{
					
				}
				
				flag=false;
			}case "n"->
			{
				
				flag=false;
			}
			default->
			{
				System.err.println("Please enter a correct choice between y/Y or n/N.");
				flag=true;
			}
			}
		}while(flag);
	}
	
}
