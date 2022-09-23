
public class WandOfKeySummoning extends Item {
	
	public WandOfKeySummoning(String name, String description) {
		super(name,description);
	}

	@Override
	public void use() {
		System.out.println("Gold sparkles burst from the wand! A gold key appears. This appears to be the key to the door.");
		UselessItem gold_key = new UselessItem("gold_key", "Key that is gold");
		getRoom().add(gold_key);;
	}

}
