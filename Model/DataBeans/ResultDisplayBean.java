package rockPaperScissors.Model.DataBeans;

import java.util.*;

import rockPaperScissors.Model.Choice;

public class ResultDisplayBean
{
	private List<Result> resultList = new ArrayList<Result>();

	public class Result
	{
		private Choice yourChoice = null;
		private Choice opponentChoice = null;
		private Integer winsOrNotInt = Integer.valueOf(-1);

		public Result()
		{
			super();
		}

		public Result(Choice yourChoice, Choice opponentChoice, Integer winOrNotInt) 
		{
			this.setYourChoice(yourChoice);
			this.setOpponentChoice(opponentChoice);
			this.setWinsOrNotInt(winOrNotInt);
		}

		public Choice getYourChoice()
		{
			return yourChoice;
		}
		public void setYourChoice(Choice yourChoice)
		{
			this.yourChoice = yourChoice;
		}
		public Choice getOpponentChoice()
		{
			return opponentChoice;
		}
		public void setOpponentChoice(Choice opponentChoice)
		{
			this.opponentChoice = opponentChoice;
		}
		public Integer getWinsOrNotInt()
		{
			return winsOrNotInt;
		}
		public void setWinsOrNotInt(Integer winsOrNotInt)
		{
			this.winsOrNotInt = winsOrNotInt;
		} 
	}

	public List<Result> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<Result> resultList)
	{
		this.resultList = resultList;
	}

	public Integer getRoundsYouWinInt()
	{
		Integer i = Integer.valueOf(0);
		for(Result r : getResultList()) 
		{
			if(r.getWinsOrNotInt().equals(2)) 
			{
				i += 1;
			}
		}
		return i;
	}

	public Integer getRoundsOpponentWinInt()
	{
		Integer i = Integer.valueOf(0);
		for(Result r : getResultList()) 
		{
			if(r.getWinsOrNotInt().equals(0)) 
			{
				i += 1;
			}
		}
		return i;
	}


	public String getGameResultText()
	{
		if(this.getRoundsYouWinInt().compareTo(getRoundsOpponentWinInt()) > 0) 
		{
			return "YOU WIN";
		}
		else if(this.getRoundsYouWinInt().compareTo(getRoundsOpponentWinInt()) < 0)
		{
			return "YOU LOSE";
		}
		else 
		{
			return "TIE";
		}
	}


	public void appendResult(Choice yourChoice, Choice opponentChoice, Integer winOrNotInt) 
	{
		resultList.add(new Result(yourChoice, opponentChoice, winOrNotInt));
	}

}
