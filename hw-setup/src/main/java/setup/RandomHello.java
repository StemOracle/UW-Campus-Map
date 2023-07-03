package setup;
import java.util.Random;
/** RandomHello selects and prints a random greeting. */
public class RandomHello {

    /**
     * Prints a random greeting to the console.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        RandomHello randomHello = new RandomHello();
        System.out.println(randomHello.getGreeting());
    }

    /** @return a greeting, randomly chosen from five possibilities */
    public String getGreeting() {
        Random randomGenerator = new Random();
	String[] greetings = new String[5];
        greetings[0] = "Ahoy!";
	greetings[1] = "Felicitations, esteemed TA! I hope you're having an excellent day and I hope you can spend some time outside in this amazing weather!";
	greetings[2] = "Bonjour Monde.";
	greetings[3] = "G'day, mate!";
	greetings[4] = "Welcome to the party, pal.";
	
	int num = randomGenerator.nextInt(5);
	return greetings[num];
    }
} 

