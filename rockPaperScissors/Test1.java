package rockPaperScissors.rockPaperScissors;

import java.text.NumberFormat;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[]myNumber= {1,2,3,4,5,6,7};
		NumberFormat nf = NumberFormat.getInstance();
		 for (int i = 0; i < myNumber.length; ++i) {
		     System.out.println(nf.format(myNumber[i]) + "; ");
		 }
		 
	}

}
