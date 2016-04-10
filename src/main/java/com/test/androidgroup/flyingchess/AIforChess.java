package com.test.androidgroup.flyingchess;


import com.test.androidgroup.flyingchess.Resource.Chess;
import com.test.androidgroup.flyingchess.Resource.Player;

public class AIforChess
{
    GameManager _gameManager;

    AIforChess(GameManager gameManager){
        _gameManager = gameManager;
    }

	private static int OneStepForward(int pos, int dir, int c)
	{

		if (pos <= 51)
		{
			if (pos%13 != 0)
				return (pos+1)%52;
			else
			{
				if (pos/13 != c)
					return (pos+1)%52;
				else
					return 52+c;
			}
		}

		if (pos/4 == 19)
			return 13*c+4;

		if (pos/4 != 18)
		{
			if (dir == 1)
				return pos+4;
			else
				return pos-4;
		}

		return pos-4;
	}

	private static int nStepsForward(int pos, int num, int c) 
	{

		if (pos >= 80)
		{
			if (num == 6)
				return 76 + c;
			else
				return pos;
		}

		int newpos = pos;
		int direction = 1;

		for (int i = 0; i < num; ++ i)
		{
			newpos = OneStepForward(newpos, direction, c);
			if (newpos/4 == 18)
				direction = 1 - direction;
		}

		return newpos;

	}

	public int WhichToGo(int num, int c)
	{
		int[] pos = new int[16];
		int[] board = new int[96];

		for (int i = 0; i < 52; ++ i)
			board[i] = -1;

        int m = 0;
        for (Player player : _gameManager.getPlayerList()) {
            for (Chess chess : player.getChessList()) {
                pos[m] = chess.getCell().getIndex();
                if (pos[m] < 52)
                    board[pos[m]] = m / 4;
                ++m;
            }
        }

		int newpos, newpos2, p;
		int[] prior = new int[4];

		for (int i = 0; i < 4; ++ i)
		{
			p = pos[c*4+i];
			prior[i] = 0;
			if(p >= 80 && num != 6){
                prior[i] = -10;
                prior[i] = -10;
            }

			newpos = nStepsForward(p, num, c);
			newpos2 = newpos;
			if (newpos%4 == c && newpos%13 != 0)
			{
				if (newpos == (20+13*c)%52 || newpos+4 == (20+13*c)%52)
					newpos2 = (newpos + 16) % 52;
				else
					newpos2 = (newpos + 4) % 52;
			}

			if (newpos2 == p)
			{
				prior[i] += 0;
				continue;
			}

			if (newpos2 / 4 == 19)
			{
				prior[i] += 2;
				continue;
			}

			if (newpos2 / 4 == 18)
			{
				prior[i] += 4;
				continue;
			}

			if (newpos2 != newpos)	
				prior[i] += 3;

			if (board[newpos] != -1 && board[newpos] != c && board[newpos2] != -1 && board[newpos2] != c)
				prior[i] += 5;

			prior[i] += 1;
		}

		int choice = 0;

		for (int i = 1; i < 4; ++ i)
		{
			if (prior[i] > prior[choice])
				choice = i;
		}

		return c*4+choice;

	}
}