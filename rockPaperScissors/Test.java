package rockPaperScissors.rockPaperScissors;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Test {
	static Semaphore semaphore = new Semaphore(1);
	public static void main(String[]args) throws InterruptedException{
		Client client=new Client();
		try 
		{

			client.initialize();

			//System.out.println("Does it initilize   "+client.getHasInitialized());
		}
		catch(IOException ioe) 
		{
			System.out.print("fail");

		}
		catch (ClassNotFoundException | NullPointerException e) 
		{
			//Invalid DataBean
			//server passed a null
			e.printStackTrace();
			//appendTextArea("Invalid Data from server!");
		}
		System.out.println("Acquiring does it initialize");
		semaphore.acquire();
		System.out.println("Does it initilize   "+client.getHasInitialized());
		//System.out.println("Does it initilize   "+client.getHasInitialized());
		System.out.println("releasing");
		semaphore.release();
		System.out.println("available Semaphore permits now: "
				+ semaphore.availablePermits());
	}
	

}
