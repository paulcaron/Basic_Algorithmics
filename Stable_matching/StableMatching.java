import java.util.PriorityQueue;
import java.util.ArrayList;

public class StableMatching implements StableMatchingInterface {

public int[][] constructStableMatching (int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs){
		 int n=0;
		 int m=menGroupCount.length;
		 int w=womenGroupCount.length;
		 int[][] matching = new int[m][w];
		 int[] aux = new int[m];					//aux[i] refers to the rank in preference order of women's group
		 int[] numberCelibGroupMen=new int[m];
		 int[] numberCelibGroupWomen=new int[w];
		 int[][] womenPrefValue = new int[w][m];	//womenPrefValue[w][m] referes to the ranking of in the preference order of w. Especially, womenPrefs[w][womenPrefValue[m]]=m and womenPrefValue[w][WomenPrefs[i]]=i    
		 PriorityQueue<PairCelib> queue=new PriorityQueue<PairCelib>(); //stores the list of men's groups starting with the highest number of singles
		 ArrayList<PriorityQueue<Integer>> queuesWomen = new ArrayList<PriorityQueue<Integer>>(); // queuesWomen[w] stores the list of the negative index of men's groups to which women of w are engaged
		 
		 for(int i=0; i<m; i++) {								// compute n, initialize numberCelibGroupMen and queue
			 n+=menGroupCount[i];
			 numberCelibGroupMen[i]=menGroupCount[i];
			 queue.add(new PairCelib(menGroupCount[i],i));
		 }
		 for(int j=0;j<w;j++) {									//initialize numberCelibGroupWomen and queueWomen
			 numberCelibGroupWomen[j]=womenGroupCount[j];
			 queuesWomen.add(new PriorityQueue<Integer>(m));
		 }
		 
		 for(int women=0;women<w;women++) {						//initialize womenPrefValue
			 for(int i=0;i<m;i++) {
				 womenPrefValue[women][womenPrefs[women][i]]=i;
			 }
		 }
		 
		 int numberCelib=n;										//initialize the number of remaining singles
		 
		 
		 
		 while(numberCelib>0) {
			 PairCelib p = queue.poll();
			 int indexMen=p.menGroupIndex;
			 if(p.nbCelib!=numberCelibGroupMen[indexMen]) continue; //check that pair p is stored and valid
			 
			 
			 boolean married = false;								//initialize a boolean to ensure that indexMen proposes to a group
			 while(!married) {
				 int indexPreference=aux[indexMen];
				 int indexWomen=menPrefs[indexMen][indexPreference];
				 if(numberCelibGroupWomen[indexWomen]>0) {
					 married=true;
					 int relationship=Math.min(numberCelibGroupWomen[indexWomen],numberCelibGroupMen[indexMen]);
					 numberCelib-=relationship;
					 numberCelibGroupMen[indexMen]-=relationship;
					 numberCelibGroupWomen[indexWomen]-=relationship;
					 matching[indexMen][indexWomen]+=relationship;
				 }
				 
				 
				 while(numberCelibGroupMen[indexMen]>0){
					 if(queuesWomen.get(indexWomen).isEmpty()){
						 aux[indexMen]++;
						 break;
					 }
					 int i = womenPrefs[indexWomen][-queuesWomen.get(indexWomen).peek()];						 
					 if(womenPrefValue[indexWomen][indexMen] < womenPrefValue[indexWomen][i]) {
						 married=true;
						 int relationship=Math.min(matching[i][indexWomen],numberCelibGroupMen[indexMen]);
						 if(relationship==matching[i][indexWomen]) queuesWomen.get(indexWomen).poll();
						 
						 matching[indexMen][indexWomen]+=relationship;
						 matching[i][indexWomen]-=relationship;
						 numberCelibGroupMen[indexMen]-=relationship;
						 numberCelibGroupMen[i]+=relationship;
						 queue.add(new PairCelib(numberCelibGroupMen[i],i));
					 }
					 else {
						 aux[indexMen]++;
						 break;
					 }
					 
		
				} 
				 if(married && numberCelibGroupMen[indexMen]>0) queue.add(new PairCelib(numberCelibGroupMen[indexMen],indexMen));
				 
				 if(married) queuesWomen.get(indexWomen).add(-(Integer)womenPrefValue[indexWomen][indexMen]);
			 
			}
			
		}
			 

	    return matching;
		 
		 
		 
		 
		 
		 
		 
	} 

static class PairCelib implements Comparable<PairCelib> {
	int nbCelib;
	int menGroupIndex;
	
	PairCelib(int nbCelib, int menGroupIndex){
		this.nbCelib=nbCelib;
		this.menGroupIndex=menGroupIndex;
	}
	
	
	public int compareTo(PairCelib o){
		return o.nbCelib-this.nbCelib;
	}
	
}

}
