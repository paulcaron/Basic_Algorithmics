import java.util.PriorityQueue;
import java.util.ArrayList;

public class StableMatching implements StableMatchingInterface {

public int[][] constructStableMatching (int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs){
		 int n=0;
		 int m=menGroupCount.length;
		 int w=womenGroupCount.length;
		 int[][] matching = new int[m][w];
		 int[] aux = new int[m];					//aux[i] d�signe le rang dans l'ordre de pr�f�rence du groupe de femmes auquel i devra se proposer au prochain passage
		 int[] numberCelibGroupMen=new int[m];
		 int[] numberCelibGroupWomen=new int[w];
		 int[][] womenPrefValue = new int[w][m];	//womenPrefValue[w][m] d�signe le classement de m dans l'ordre de pr�f�rence de w. En particulier : womenPrefs[w][womenPrefValue[m]]=m et womenPrefValue[w][WomenPrefs[i]]=i    
		 PriorityQueue<PairCelib> queue=new PriorityQueue<PairCelib>(); //stocke la liste des groupes d'hommes en partant de celui avec le plus grand nombre de c�libataires
		 ArrayList<PriorityQueue<Integer>> queuesWomen = new ArrayList<PriorityQueue<Integer>>(); // queuesWomen[w] stocke la liste des oppos�s des indices des groupes d'hommes avec lesquels des femmes du groupe w sont engag�s
		 
		 for(int i=0; i<m; i++) {								// calcul de n, initialisation de numberCelibGroupMen et queue
			 n+=menGroupCount[i];
			 numberCelibGroupMen[i]=menGroupCount[i];
			 queue.add(new PairCelib(menGroupCount[i],i));
		 }
		 for(int j=0;j<w;j++) {									//initialisation de numberCelibGroupWomen et queueWomen
			 numberCelibGroupWomen[j]=womenGroupCount[j];
			 queuesWomen.add(new PriorityQueue<Integer>(m));
		 }
		 
		 for(int women=0;women<w;women++) {						//initialisation de womenPrefValue
			 for(int i=0;i<m;i++) {
				 womenPrefValue[women][womenPrefs[women][i]]=i;
			 }
		 }
		 
		 int numberCelib=n;										//initialisation du njombre de c�libataires restants
		 
		 
		 
		 while(numberCelib>0) {
			 PairCelib p = queue.poll();
			 int indexMen=p.menGroupIndex;
			 if(p.nbCelib!=numberCelibGroupMen[indexMen]) continue; //on v�rifie que p est bien une paire stock�e valide
			 
			 
			 boolean married = false;								//on initialise un boolean qui permettra qui s'assurera que indexMen se propose � un groupe de femmes
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
