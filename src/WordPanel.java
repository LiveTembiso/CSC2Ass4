import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
                private int counter; //number of words that have been dropped

		
		public void paintComponent(Graphics g) {
                    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
	
		   if(!done){
			//System.out.println(done);
			for (int i=0;i<noWords;i++){	    	
				g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
			}
		   }
		   else{
			g.drawString(WordApp.finishLine, width/3, height/2);
		   }	
		   
		}
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;	
                        counter = 0;
		}
                
                public void setDone()
                {
                    done = true;
                }
                
                public void undone()
                {
                    done = false;
                }
                
                public synchronized boolean isDone()
                {
                    return done;
                }
                
                public synchronized int getCounter()
                {
                    return counter;
                }
                
                public synchronized void resetCounter()
                {
                    counter = 0;
                }
		
                @Override
		public void run() {
                    undone();
                    while(!done)
                    {
                        try{
                            Thread.sleep(100);
                        } 
			
			catch (InterruptedException e){
                            System.out.println(e);
                        }

                        for (int i = 0; i < noWords; i++){
                            int spid = words[i].getSpeed();
                            words[i].drop(spid/180);
                            if (words[i].dropped()){
                                counter++;
                                words[i].resetWord();
                            }
                        }
                        repaint();
                    }

                    counter = 0;
                    for (int w = 0; w < noWords; w++)
                    {
                        words[w].resetWord();
                    }
                    repaint();
		}

	}


