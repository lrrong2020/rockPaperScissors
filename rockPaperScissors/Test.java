package rockPaperScissors.rockPaperScissors;

import java.io.IOException;


public class Test {
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
		client.initSemaphore.acquire();
		System.out.println("Does it initilize   "+client.getHasInitialized());
		//System.out.println("Does it initilize   "+client.getHasInitialized());
		System.out.println("releasing");
		client.initSemaphore.release();
		System.out.println("available Semaphore permits now: "
				+ client.initSemaphore.availablePermits());
	}
	

}
