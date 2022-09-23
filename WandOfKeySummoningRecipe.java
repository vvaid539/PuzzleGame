
public class WandOfKeySummoningRecipe extends Recipe {

	public WandOfKeySummoningRecipe() {
		super(false, "runed_stick", "phoenix_feather", "sapphire", "unicorn_tears");
	}

	@Override
	public void combineInRoom(Room room) {
		System.out.println("You created a wand_of_key_summoning!\n"
				+ "");
		 WandOfKeySummoning wand = new WandOfKeySummoning("wand_of_key_summoning", "a magical wand that summons a key");
		 wand.setRoom(room);
		 room.add(wand);
		 room.remove(room.getItem("phoenix_feather"));
		 room.remove(room.getItem("sapphire"));
		 room.remove(room.getItem("unicorn_tears"));
		 room.remove(room.getItem("runed_stick"));
		
	}

}
