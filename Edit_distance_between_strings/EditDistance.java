import java.util.*;

public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;
    

    public EditDistance (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    public int[][] getEditDistanceDP(String s1, String s2) {
        int[][] d = new int[s1.length()+1][s2.length()+1];

            for(int i = 1; i <= s2.length(); i++){
            	d[0][i] = d[0][i-1] + c_i;
            }
            for(int i = 1; i <= s1.length(); i++){
            	d[i][0] = d[i-1][0] + c_d;
            }
            
            for(int i = 1; i <= s1.length(); i++){
                for(int j = 1; j <= s2.length(); j++){
                	if(s1.charAt(i-1)==s2.charAt(j-1)){
                		d[i][j] = d[i-1][j-1];
                	}else{
                		d[i][j] = Math.min(d[i-1][j-1] + c_r, Math.min(d[i-1][j] + c_d, d[i][j-1] + c_i));
                	}
                }
            }
        
        return d;
    }
    
    public int[][] getEditDistanceDPOptimized(String s1, String s2) {
        int[][] d=new int[1][1];
        int X=1;
        int minDist;
        int maxGap=0;
        int jmax = 0;
        boolean flag = true;
        
  
        do{
        	if(flag){
        		minDist = 0;
        		flag = false;
        	}else{
        		minDist = d[s1.length()][jmax];
        	}
        	 
        	maxGap = Math.abs(s2.length() - s1.length()) + X;
        	
            d = new int[s1.length()+1][2*maxGap + 1];
            
            for(int i = maxGap + 1; i <= 2*maxGap; i++){
            	d[0][i] = d[0][i-1] + c_i;
            }
            for(int i = 1; i <= maxGap; i++){
            	d[i][maxGap-i] = d[i-1][maxGap-i+1] + c_d;
            }
            
            for(int i = 1; i <= s1.length(); i++){
            	jmax = maxGap + Math.min(maxGap, s2.length() - i);
                for(int j = Math.max(0, maxGap - i + 1); j <= jmax; j++){
                	if(s1.charAt(i-1) == s2.charAt(i-maxGap+j-1)){
                		d[i][j] = d[i-1][j];
                	}else{
                		if(j == jmax){
                			d[i][j] = Math.min(d[i-1][j] + c_r, d[i][j-1] + c_i);
                		}else if(j == 0){
                			d[i][j] = Math.min(d[i-1][j] + c_r, d[i-1][j+1] + c_d);
                		}else{
                			d[i][j] = Math.min(d[i-1][j] + c_r, Math.min(d[i-1][j+1] + c_d, d[i][j-1] + c_i));
                		}
                	}
                }
            }
            X *= 2;
        } while(minDist != d[s1.length()][jmax]); //if the two values are different, we double X
        
        return d;
    }

    public List<String> getMinimalEditSequence(String s1, String s2) {
        LinkedList<String> ls = new LinkedList<> ();

        int[][] d=new int[1][1];
        int[][] op; 
        int X = 1;
        int minDist=0;
        int maxGap=0;
        int jmin=0;
        int jmax=0;
        boolean flag = true;
        
        do{
        	if(flag){
        		flag = false;
        	}
        	else{
        		minDist = d[s1.length()][jmax];
        	}
        	
        	maxGap = Math.abs(s2.length() - s1.length()) + X;
        	
            d=new int[s1.length()+1][2*maxGap + 1];
            op=new int[s1.length()+1][2*maxGap + 1];
            
            for(int j=maxGap + 1; j <= 2*maxGap; j++){
            	d[0][j]=j*c_i;
            	op[0][j]=3;
            }
            for(int i = 1; i <= maxGap; i++){
            	d[i][maxGap-i]=d[i-1][maxGap-i+1]+c_d;
            	op[i][maxGap-i]=2;
            }
            
            for(int i = 1; i <= s1.length(); i++){
            	jmin=Math.max(0, maxGap-i+1);
            	jmax=Math.min(2*maxGap, maxGap+s2.length()-i);
                for(int j=jmin ; j <= jmax; j++){
                	if(s1.charAt(i-1) == s2.charAt(i-maxGap+j-1)){
                		d[i][j]=d[i-1][j];
                	}
                	else{
                		
                		d[i][j]=d[i-1][j] + c_r;
                		op[i][j]=1;
         
                		if(j==jmax){
                			if(d[i][j] > d[i][j-1] + c_i){
                				d[i][j] = d[i][j-1] + c_i;
                				op[i][j] = 3;
                			}
                		}
                		else if(j == 0){
                			if(d[i][j] > d[i-1][j+1] + c_d){
                				d[i][j] = d[i-1][j+1] + c_d;
                				op[i][j] = 2;
                			}
                		}
                		else{
                			if(d[i][j] > d[i-1][j+1] + c_d){
                				d[i][j] = d[i-1][j+1] + c_d;
                				op[i][j] = 2;
                			}
                			if(d[i][j] > d[i][j-1] + c_i){
                				d[i][j] = d[i][j-1] + c_i;
                				op[i][j] = 3;
                			}
                		}
                	}
                }
            }
            X=2*X;
        } while(minDist != d[s1.length()][jmax]); 
        
        int i = s1.length();
        int j = jmax;
        while(i != 0 || j != maxGap){

        	if(op[i][j]==0){
        		i--;
        	}	
        	if(op[i][j]==1){
        		ls.add("replace(" + (i-1) + "," + s2.charAt(i+j-maxGap-1) +")");
        		i--;
        	}
        		
        	if(op[i][j]==2){
        		ls.add("delete(" + (i-1) + ")");
        		j++;
        		i--;
        	}
        		
        	if(op[i][j]==3){
        		ls.add("insert(" +i+ "," + s2.charAt(i+j-maxGap-1) +")");
        		j--;
        	}
        		
        	
        }
        
        
        return ls;
    }
}
