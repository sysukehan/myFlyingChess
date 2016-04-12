package com.test.androidgroup.flyingchess;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.view.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.test.androidgroup.flyingchess.Resource.*;
import com.test.androidgroup.flyingchess.Server.MSGS;
import com.test.androidgroup.flyingchess.Server.MessageProcess;
import com.test.androidgroup.flyingchess.Server.MessageProcessForUI;

public class GameManager {
	private ArrayList<Player> _playerList;
	private Player mainPlayer;
	private Player currentPlayer;
	private int currentPlayerIndex;
	private Dice dice;
	public boolean canRoll;
	private int steps;
	private ArrayList<Cell> _gameBoard;
	GameActivity _gameActivity;
	AIforChess AI = new AIforChess(this);
	public Handler handler;
	MessageProcess mp;//初始化mp
	private boolean online = false;
	
	private static final String exceptionMsg = "Don't be naughty dumbass";
	public GameManager(Player player,GameActivity gameActivity, boolean _online) {
		_playerList = new ArrayList<Player>();
		mainPlayer = player;
		canRoll = false;
		_gameActivity = gameActivity;
		online = _online;
	}

	public ArrayList<Player> getPlayerList(){
		return _playerList;
	}

	public void addPlayer(Player player) throws Exception {
		if (_playerList.size()>=4) {
			throw new Exception(exceptionMsg);
		}
		_playerList.add(player);
	}

	/**
	 * @param seed
	 * which is the same for 4 clients, received from server
	 */
	public void init(int seed) throws Exception {
		if (_playerList.size()<1) {
			throw new Exception(exceptionMsg);
		}
		// the seed should be the same for 4 client, get from the server
		dice = new Dice(seed);

		handler = new Handler() {
			public void handleMessage(Message msg){
				//Log.i("handleMessage",Integer.toHexString(msg.what));
                if(msg.what == 0x006){//设置左下角当前玩家棋子
                    _gameActivity.setF_color(currentPlayerIndex % _playerList.size());
                }
                if(msg.what == 0x007){//开始摇骰子动画
                    _gameActivity.startRollDiceAnimation();
                }
				if(msg.what == 0x100){//设置准备摇骰子
					_gameActivity.setRollReady();
				}
				if(msg.what == 0x101){//单机模式获得摇骰子结果
					steps = msg.getData().getInt("rollResult");
					getStepsAndDeal();
				}
				if(msg.what == 0x102){//单机模式开始走棋子
					getMoveAndDeal(msg.getData().getInt("chessToMove"),msg.getData().getInt("targetCell"));
				}
				if(msg.what == 0x103){//移动棋子消息
					Log.i("handleMessage",Integer.toHexString(msg.what));
					_gameActivity.moveToNext(msg.getData().getInt("chessToMove"), msg.getData().getInt("preCell"), msg.getData().getInt("targetCell"),300);
				}
				if(msg.what == 0x104){//设置完成棋子
					_gameActivity.setDoneCell(msg.getData().getInt("chessRid"));
				}
				if (msg.what == 0x123)//是正常的消息
				{
					try
					{
						MSGS.MSG m = MSGS.MSG.parseFrom((byte[])msg.obj);   //获取消息类
						if(m.getMessageType() == MSGS.MSGType.dicePoint_Notification){//摇骰子结果
							MSGS.DicePointNtf cr = m.getNotification().getDicePointNtf();
							dice.rollresult = cr.getPoint();
							steps = dice.rollresult;
							getStepsAndDeal();
						}

						if(m.getMessageType() == MSGS.MSGType.chessMove_Notification){//移动棋子
							MSGS.ChessMoveNtf cr = m.getNotification().getChessMoveNtf();

							getMoveAndDeal(cr.getChessMove().getChessID(),cr.getChessMove().getDestination());
						}
					}
					catch (Exception e){Log.i("connectError:",e.toString());}
				}
				if (msg.what == 0x130)//无法建立连接
				{
					Log.i("connect", "建立连接失败");
					//new AlertDialog.Builder(MainActivity.this).setTitle("建立连接失败").setPositiveButton("OK", null).create().show();
				}
				if (msg.what == 0x131)//连接断开
				{
					Log.i("connect","失去连接");
					//new AlertDialog.Builder(MainActivity.this).setTitle("失去连接").setPositiveButton("OK", null).create().show();
				}
			}
		};
		mp = RunningInformation.mp;
		mp.sendHandler = handler;
		mp.ms.sendHandler = handler;
		/*
		if(online) {
			mp.start();//开启监听来自服务器消息的线程，而ms随后也会自动被建立。
		}
		*/
		// set current player
		//currentPlayerIndex = dice.roll() % _playerList.size();//
		currentPlayerIndex = 0;//测试，从当前用户开始
		currentPlayer = _playerList.get(currentPlayerIndex);
		canRoll = isMyTurn();
        startNewTurn();
		
		// load map
		_gameBoard = new ArrayList<Cell>();
		
		for (int i = 0; i < 96; ++i) {
			_gameBoard.add(new Cell(i));
		}

		try{
			InputStream in = _gameActivity.getResources().openRawResource(R.raw.pos);
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader bufferedReader = new  BufferedReader(reader);
			StringBuffer string_buffer = new StringBuffer("");
			String str = bufferedReader.readLine();
			;
			while((str = bufferedReader.readLine()) != null){
				String[] s = str.split(",");
				Cell nextCell = null;
				Cell nextCell_2 = null;
				Cell nextSameColorCell = null;
				if (Integer.parseInt(s[2]) > -1) {
					nextCell = _gameBoard.get(Integer.parseInt(s[2]));
				}
				if (Integer.parseInt(s[3]) > -1) {
					nextCell_2 = _gameBoard.get(Integer.parseInt(s[3]));
				}
				if (Integer.parseInt(s[4]) > -1) {
					nextSameColorCell = _gameBoard.get(Integer.parseInt(s[4]));
				}
				_gameBoard.get(Integer.parseInt(s[0])).init(
						Color.valueOf(s[1]),
						nextCell,
						nextCell_2,
						nextSameColorCell,
						Integer.parseInt(s[5]) == 1,
						Integer.parseInt(s[6]) == 1,
						Integer.parseInt(s[7]) == 1,
						Integer.parseInt(s[8]) == 1);
			}
			in.close();
		} catch (Exception e){
			e.printStackTrace();
		}

		int temp_i = 0;
		for(Player player : _playerList){
			for(Chess chess : player.getChessList()){
				chess.setId(temp_i);
				temp_i++;
			}
		}

		// put chesses
		for (Player player : _playerList) {
			for (Chess chess : player.getChessList()) {
				sendToHome(chess);
			}
		}
	}

	private void getMoveAndDeal(int chessId, int cellId){
		Chess chessToMove = null;
		for(Player player : _playerList){
			if(chessToMove != null) break;
			for(Chess chess : player.getChessList()){
				if(chess.getId() == chessId){
					chessToMove = chess;
					break;
				}
			}
		}
		Cell cellToMove = null;
		for(Cell cell : _gameBoard){
			if(cellToMove != null) break;
			if(cell.getIndex() == cellId){
				cellToMove = cell;
				break;
			}
		}

		move(chessToMove, cellToMove);
	}

	private void getStepsAndDeal() {
		Log.i("steps", String.valueOf(steps));
		_gameActivity.setRollResult(steps);
		if (isMyTurn()) {
			if (!canMove(mainPlayer) && steps != 6) {
				endTurn();
			} else {
				_gameActivity.canMove = true;
			}
		} else {
			if (currentPlayer.is_Bot) {
				if (!canMove(currentPlayer) && steps != 6) {
					endTurn();
				} else {
					int choiceChessID = AI.WhichToGo(steps, currentPlayer.getColor().ordinal());
					Chess _chessToMove = null;
					for (Player player : _playerList) {
						if (_chessToMove != null) break;
						for (Chess chess : player.getChessList()) {
							if (chess.getId() == choiceChessID) {
								_chessToMove = chess;
								break;
							}
						}
					}
                    dealTheMove(_chessToMove,steps);

				}
			}
		}
	}

    private void dealTheMove(final Chess chessToMove, final int moveStep){
		chessToMove._jumped = false;
		chessToMove.is_goingBack = false;
		chessToMove.is_finishedThisTurn = false;
		final int startCell = chessToMove.getCell().getIndex();
		Log.i("MoveInf",chessToMove.getId() + " " + moveStep);
		Thread keepSendMoveReq = new Thread(new Runnable() {
            public void run() {
                try{
                    for(int i = 0; i < moveStep; ++i){
                        if(chessToMove.getCell().is_Home()){//出新机，消耗所有步数
                            i = moveStep - 1;
                        }
                        Cell targetCell = getTheNextCell(chessToMove);
						if(targetCell != null && targetCell != chessToMove.getCell()){
                            sendMoveReq(chessToMove,targetCell);
                        }
                        Thread.sleep(300);
                    }
                    while(true){
						if(chessToMove.getCell().getIndex() == startCell){
							chessToMove.is_finishedThisTurn = true;
							break;
						}
                        Cell newCell = simulateMove(chessToMove);
						//if(newCell != null) 	Log.i("newCell",String.valueOf(newCell.getIndex()));
                        if(newCell != null && newCell != chessToMove.getCell()){
                            sendMoveReq(chessToMove,newCell);
                        }
                        else{
							break;
						}
						Thread.sleep(300);
                    }
					dealTheClash();
					Thread.sleep(500);
					endTurn();

                }
                catch (Exception e){}
            }
        });
        keepSendMoveReq.start();
    }

    private void sendMoveReq(Chess chessToMove,Cell targetCell){
        if(online) {
            MessageProcessForUI.sendChessMoveReq("123456", 1, MSGS.Color.values()[chessToMove.getColor().ordinal()], chessToMove.getId(), targetCell.getIndex(), mp.ms.sendHandler);
        }
        else{
            Message msg = new Message();
            msg.what = 0x102;
            Bundle bundle = new Bundle();
            bundle.putInt("chessToMove",chessToMove.getId());
            bundle.putInt("targetCell",targetCell.getIndex());
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

	public boolean isGameOver() {
		for (Player player : _playerList) {
			int count = 0;
			for (Chess chess : player.getChessList()) {
				if (chess.is_Completed) {
					++count;
				}
			}
			if (count == 4) return true;
		}
		return false;
	}
	
	public void endTurn() {
		Log.i("system", "Turn End");
		if (isGameOver()) {
			// gameover
			
			return;
		}

		if (steps != 6) {
			currentPlayer = _playerList.get(++currentPlayerIndex % _playerList.size());
		}
        Thread waitToNewTurn = new Thread(new Runnable() {
            public void run(){
                try{
                    Thread.sleep(500);
                    startNewTurn();

                }
                catch (Exception e){}
            }
        });
        waitToNewTurn.start();

	}

	private void dealTheClash(){
		for(Chess chess : currentPlayer.getChessList()){
			if(chess.getCell().getChessList().size() > 1){
				ArrayList<Chess> chessToHome = new ArrayList<>();
				for(Chess chessToDeal : chess.getCell().getChessList()){
					if(chessToDeal.getColor() != currentPlayer.getColor()){
						chessToHome.add(chessToDeal);
					}
				}
				for(Chess chessToDeal : chessToHome){
					sendToHome(chessToDeal);
				}
			}
		}
	}

    private void startNewTurn() {
        steps = 0;
        canRoll = isMyTurn();
        _gameActivity.canMove = false;
        handler.sendEmptyMessage(0x006);
        handler.sendEmptyMessage(0x100);

        Log.i("PlayerColor", currentPlayer.getColor().toString());
        if (currentPlayer.is_Bot) {
            handler.sendEmptyMessage(0x007);
            SendRollIfRoomMaster();
        }
    }

	private void SendRollIfRoomMaster(){
		//if(mainPlayer.isRoomMaster){
		if(online) {
			dice.roll(mp.ms.sendHandler);
		}
		else{
			dice.rollOffline(handler);
		}
	}

	public int findTheMoveChessAndMove(int rid){
		Cell targetCell = null;
		for(Player player : _playerList){
			if(targetCell != null) break;
			for(Chess chess : player.getChessList()){
				if(chess._rid == rid){
					if(chess.getColor() != mainPlayer.getColor()){
						return  0;
					}
                    if (!chess.getCell().is_Home() || steps == 6) {
                        dealTheMove(chess,steps);
                        return 0;
                    }
					break;
				}
			}
		}
        return -1;
	}

	/**
	 * simulate movement
	 * @param chess needed to be handled
	 * @return destination Cell, if feasible, else return null
	 * if the returned cell's is_Done() returns true, it means it's good to rest
	 */

	public Cell getTheNextCell(Chess chess){
		if(chess.getCell().getIndex() < 40 && chess.getCell().getIndex() % 13 == 0 && chess.getCell().getColor() == chess.getColor()){//在向内部区域的拐点
			return chess.getCell().getNextCell_2();
		}
        if(chess.is_goingBack){//如果棋子在往回走(在中间区域)
            if(chess.getCell().getNextCell_2().getChessList().size() > 1)//下一格被阻塞
                return chess.getCell();
            else
                return chess.getCell().getNextCell_2();
        }
        else {
            if(chess.getCell().getNextCell().getChessList().size() > 1 && chess.getCell().getIndex() < 80)//下一格被阻塞，出飞机时不会被阻塞
                return chess.getCell();
            else
                return chess.getCell().getNextCell();
        }
    }

    //移动结束后计算跳棋或飞棋
	public Cell simulateMove(Chess chess) {
        Cell tmpCell = chess.getCell();
        if (tmpCell.is_Done()) {
            chess.is_Completed = true;
			sendToHome(chess);
			Message msg = new Message();
			msg.what = 0x104;
			Bundle bundle = new Bundle();
			bundle.putInt("chessRid",chess._rid);
			msg.setData(bundle);
			handler.sendMessage(msg);
			return chess.getCell();
		}
        if (String.valueOf(tmpCell.getColor()) == String.valueOf(chess.getColor()) && chess.getCell().getIndex() < 52) {//如果为同一颜色，即可以跳或飞
            if (tmpCell.is_Flyable()) {//飞
                if (tmpCell.getNextSameColorCell().getChessList().size() > 1) {//当对面的格子被阻塞
                    return tmpCell;
                }
                else {
					if(tmpCell.getNextCell_2().getChessList().size() == 1){
						sendToHome(tmpCell.getChessList().iterator().next());
					}
                    return tmpCell.getNextSameColorCell();
                }
            } else {//跳跃
                if (chess._jumped) {//如果在本次移动中已跳跃
                    return tmpCell;
                } else {
                    if (tmpCell.getNextSameColorCell() != null && tmpCell.getNextSameColorCell().getChessList().size() > 1) {//跳跃目标被阻塞
                        return tmpCell;
                    } else {
                        chess._jumped = true;
                        return tmpCell.getNextSameColorCell();
                    }
                }
            }
        }
        return tmpCell;
    }
	
	public int rollDice() {
		if (canRoll) {
			if(online){
				dice.roll(mp.ms.sendHandler);
			}
			else{
				dice.rollOffline(handler);
			}
			canRoll = false;
			return 0;
		}
		// error
		return -1;
	}

	public boolean canMove(Player target_player){
		for(Player player: _playerList){
			if(player == target_player){
				for(Chess chess : player.getChessList()){
					if(!chess.getCell().is_Home() && !chess.is_Completed){
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isMyTurn() {
		return mainPlayer == currentPlayer;
	}
	
	public Cell sendToHome(Chess chess) {
		Log.i("sendToHome","true");
		for (Cell cell: _gameBoard) {
			if (cell.getColor() == chess.getColor() && cell.is_Home() && cell.getChessList().isEmpty()) {
				Log.i("sendToHome","true");
				move(chess, cell);
                return cell;
			}
		}
        return null;
	}
	
	
	private void move(Chess chess, Cell cell) {
		int preCell = -1;
		// remove from previous cell
		if (chess.getCell() != null) {
			preCell = chess.getCell().getIndex();
			chess.getCell().getChessList().remove(chess);
		}
		// add to new cell
		cell.getChessList().add(chess);
		chess.setCell(cell);
		if(cell.is_Done()){
			chess.is_goingBack = true;
		}
/*		if (cell.getChessList().isEmpty() || cell.getChessList().iterator().next().getColor() == chess.getColor()) {
			// empty or same color

		} else {
			Log.i("eat","true");
			// occupied by some other color
			// should only have one other chess; if more, the code should not run to here
			if(chess.is_finishedThisTurn) {
				sendToHome(cell.getChessList().iterator().next());
			}
			cell.getChessList().add(chess);
			chess.setCell(cell);
		}*/
		if(preCell != -1) {
			//_gameActivity.moveToNext(chess.getId(), preCell, chess.getCell().getIndex(),300);
			Message msg = new Message();
			msg.what = 0x103;
			Bundle bundle = new Bundle();
			bundle.putInt("chessToMove",chess._rid);
			bundle.putInt("preCell", preCell);
			bundle.putInt("targetCell",chess.getCell().getIndex());
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}

}
