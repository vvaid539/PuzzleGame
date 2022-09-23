
public class WizardLabDriver {

    public static void main(String[] args) {
        String intro = "Welcome to the Wizard Laboratory!\n" +
                  "You have just broken into your magic professor's laboratory\n" +
                  "(without his knowledge!) in the early hours of the morning.\n" +
                  "Unfortunately, the door magically seals itself behind you\n" +
                  "and you estimate that you have a couple of hours to explore\n" +
                  "and escape before he wakes up.  Get what you need and get out!";
        WizardsLab lab = new WizardsLab("This is a wizards lab.", intro, 15);
        new EscapeApp(lab).runGame();
    }

}
