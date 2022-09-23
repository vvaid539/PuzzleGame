import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Room implements CommandHandler {

	/** A list of items in the room */
	private List<Item> items;

	/** A list of recipes that define which items can be combined in the room */
	private List<Recipe> recipes;

	/** A description of the room that is printed at the beginning of the game and also when the look command is executed. */
	private String description;

	/** The introduction printed at the beginning of the game that describes the scenario for this room. */
	private String intro;

	/** A reference to the EscapeApp running the game */
	private EscapeApp app;

	/**
	 * Creates a room with the given description and intro
	 * @param description The room description
	 * @param intro the introduction describing the scenario for this room
	 */
	public Room(String description, String intro) {
	    items = new ArrayList<>();
	    recipes = new ArrayList<>();
	    this.description = description;
	    this.intro = intro;
	}

	/**
	 * Adds an item to the room. If the item implements the CommandHandler interface and the app
	 * has been set, the item is also added to the list of CommandHandlers for the app.
	 * @param item the item to add
	 * @throws IllegalArgumentException if the room already contains an item with the same name
	 */
	public void add(Item item) {
	    if (getItem(item.getName()) != null) {
	        throw new IllegalArgumentException("This room already contains a " + item.getName());
	    }
	    item.setRoom(this);
	    items.add(item);
	    if (app != null && item instanceof CommandHandler) {
	        app.addHandler((CommandHandler)item);
	    }
	}

	/**
	 * Removes the item from the room. If the app has been set, the item is also
	 * removed from the list of CommandHandlers.
	 * @param item the item to remove
	 */
	public void remove(Item item) {
	    items.remove(item);
	    if (app != null && item instanceof CommandHandler) {
	        app.removeHandler((CommandHandler)item);
	    }
	    item.setRoom(null);
	}

	/**
	 * Returns the item in the room with the given name, or null if no such item exists.
	 * @param name the name of the item
	 * @return the item in the room with the given name, or null if no such item exists.
	 */
	public Item getItem(String name) {
	    for (Item item : items) {
	        if (item.getName().equals(name)) return item;
	    }
	    return null;
	}

	/**
	 * Returns a shallow copy of the list of items in the room. A shallow copy just means that a new list
	 * is returned that contains all the same items as the original. Changing the order of the items in the
	 * copy or adding or removing items from the copy will not be reflected in the original list of items 
	 * for the room because the copy is a different list; however, the items in the copy are direct references to
	 * the actual items in the room, so changing the properties of an item contained in the copy will still change
	 * the properties of the item in the room - they are the same objects. We do not give direct access to the array
	 * because that would mean the list of items could be modified directly, bypassing the add(item) and remove(item)
	 * methods that perform additional actions that need to be done whenever an item is added or removed from the room.
	 * It is good practice to protect your class from being broken by misuse.
	 * @return a shallow copy of the list of items in the room.
	 */
	public List<Item> getItems() {
	return new ArrayList<>(items);
	}

	/**
	 * Adds a recipe to the list of recipes in the room.
	 * @param recipe the recipe to add
	 */
	public void add(Recipe recipe) {
	recipes.add(recipe);
	}

	/**
	 * Removes a recipe from the list of recipes in the room.
	 * @param recipe the recipe to remove
	 */
	public void remove(Recipe recipe) {
	    recipes.remove(recipe);
	}

	/**
	 * Returns a shallow copy of the list of recipes in the room. See getItems() for more information on
	 * what it means to be a shallow copy.
	 * @return a shallow copy of the list of recipes in the room.
	 */
	public List<Recipe> getRecipes() {
	    return new ArrayList<>(recipes);
	}

	/**
	 * Prints the room description, including a list of all the items in the room.
	 */
	public void printDescription() {
	    System.out.println(description);
	    listItems();
	}

	/**
	 * Prints the introduction describing the scenario for this room.
	 */
	public void printIntro() {
	    System.out.println(intro);
	}

	/**
	 * Prints the list of items in the room.
	 */
	public void listItems() {
	    System.out.println("\nYou can see:");
	    for (Item i: items) {
	        System.out.println("  " + i);
	    }
	}

	/**
	 * Returns a reference to the app.
	 * @return a reference to the app
	 */
	public EscapeApp getApp() {
	    return app;
	}

	/**
	 * Sets the app
	 * @param app the app
	 */
	public void setApp(EscapeApp app) {
	    // remove handlers from old app
	    if (this.app != null) {
	        this.app.removeHandler(this);
	        for (Item item : items) {
	            if (item instanceof CommandHandler) {
	                getApp().removeHandler((CommandHandler)item);
	            }
	        }
	    }
	    this.app = app;
	    // add handlers to new app
	    app.addHandler(this);
	    if (app != null) {
	        for (Item item : items) {
	            if (item instanceof CommandHandler) {
	                getApp().addHandler((CommandHandler)item);
	            }
	        }
	    }
	}

	/**
	 * Attempts to combine the given items. If any recipes in this room
	 * match the items, the items will be combined by calling combineInRoom
	 * on the recipe, passing this room. If the items don't match a recipe,
	 * the onCombineFailed method will be called.
	 * @param items the items to attempt to combine
	 */
	public void combine(List<Item> items) {
	    for (Recipe recipe : recipes) {
	        if (recipe.matchesItems(items)) {
	            recipe.combineInRoom(this);
	            return;
	        }
	    }
	    onCombineFailed(items);
	}

	/**
	 * Executed whenever a combine attempt fails. This method can be overridden
	 * for custom behavior, but the default is to just inform the user that
	 * nothing happens.
	 * @param items the items that failed to combine
	 */
	public void onCombineFailed(List<Item> items) {
	    System.out.println("Nothing happens.");
	}
	
	@Override
	public boolean execute(String command) {
		if (command.equals("look")) {
		    printDescription();
		    return true;
		} else {
		    Scanner scan = new Scanner(command);
		    if (scan.hasNext()) {
		        String com = scan.next();
		        if (com.equals("use")) {
		            if (!scan.hasNext()) {
		                System.out.println("Use what?");
		            } else {
		                String itemName = scan.nextLine().trim();
		                Item item = getItem(itemName);
		                if (item == null) {
		                    System.out.println("There is no " + itemName + " here.");
		                } else {
		                    item.use();
		                }
		            }
		            scan.close();
		            return true;
		        } else if (com.equals("combine")) {
		            List<Item> stuff = new ArrayList<>();
		            while(scan.hasNext()) {
		                String itemName = scan.next();
		                Item item = getItem(itemName);
		                if (item == null) {
		                    System.out.println("There is no " + itemName + " here.");
		                    scan.close();
		                    return true;
		                }
		                stuff.add(item);
		            }
		            scan.close();
		            if (stuff.size() == 0) {
		                System.out.println("Combine what?");
		            } else if (stuff.size() == 1) {
		                System.out.println("Combine " + stuff.get(0).getName() + " with what?");
		            } else {
		                System.out.print("You attempt to combine the following items: " + stuff.get(0).getName());
		                for (int i = 1; i < stuff.size(); i++) {
		                    Item item = stuff.get(i);
		                    System.out.print(", " + item.getName());
		                }
		                System.out.println();
		                combine(stuff);
		            }
		            return true;
		        }
		    }
		    scan.close();
		}
		return false;

	}
	
	@Override
	public void printHelp() {
		System.out.println("look prints the room description");
		System.out.println("use <item> uses an item");
		System.out.println("combine <item1> <item2> ... attempts to combine a list of items");
	}
	
	public abstract void printRoomPrompt();
	
	public abstract void onCommandAttempted(String command, boolean value);
	
	public abstract boolean escaped();
	
	public abstract boolean failed();
	
	public abstract void onEscaped();
	
	public abstract void onFailed();
}
