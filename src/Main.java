import java.util.ArrayList;

public class Main {
	 public static void main(String[] args)
    {
		VDirectory directory = new VDirectory();
		directory.set_DirectoryName("root");
		directory.set_Deleted(false);

		ArrayList<Integer>arr = new ArrayList<>();
		for(int i=0;i<5;i++)arr.add(i);

		directory.addNewDirectory("root/sayed#1");
		directory.addNewDirectory("root/ahmed#2");
		directory.addNewDirectory("root/mahmoud#3");

		directory.addNewFile("root/ahmed#2/fileahmed#2_1",50,arr);
		directory.addNewFile("root/sayed#1/filesayed#1_1",48,arr);
		directory.addNewFile("root/mahmoud#3/filemahmoud#3_1",10,arr);

		directory.addNewDirectory("root/sayed#1/obama#1_1");
		if(!directory.addNewDirectory("root/mahmoud#3/zizo#3_1")) System.out.println("aaaaaaaaaaaaaaa");

		directory.addNewFile("root/mahmoud#3/filemahmoud#3_2",14,arr); // why
		directory.addNewFile("root/sayed#1/filesayed#1_2",17,arr);

		System.out.println("Disk structure now is : ");
		directory.printDiskStructure(" ");
	}
}
