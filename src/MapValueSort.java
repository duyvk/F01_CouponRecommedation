import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * The Class MapValueSort.
 * Phase:
 * Purpose:
 * Input:
 * Output:
 */
public class MapValueSort {
	
	/**
	 * inner class to do soring of the map *.
	 */
	static class ValueComparer implements Comparator<Integer> {
		
		/** The data. */
		private Map<Integer, Integer>  data = null;
		
		/**
		 * Instantiates a new value comparer.
		 *
		 * @param data the data
		 */
		public ValueComparer (Map<Integer, Integer> data){
			super();
			this.data = data;
		}
		
         /* (non-Javadoc)
          * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
          */
         public int compare(Integer o1, Integer o2) {
             return -(data.get(o1) - data.get(o2));
         }
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		
		Map<Integer, Integer> unsortedData = new HashMap<Integer, Integer>();
		unsortedData.put(1, 4);
		unsortedData.put(2, 3);
		unsortedData.put(5, 2);
		unsortedData.put(4, 1);
		
		
		SortedMap<Integer, Integer> sortedData = new TreeMap<Integer, Integer>(new MapValueSort.ValueComparer(unsortedData));
		
		printMap(unsortedData);
		
		sortedData.putAll(unsortedData);
		System.out.println();
		printMap(sortedData);
	}

	/**
	 * Prints the map.
	 *
	 * @param data the data
	 */
	private static void printMap(Map<Integer, Integer> data) {
		for (Iterator<Integer> iter = data.keySet().iterator(); iter.hasNext();) {
			Integer key = iter.next();
			System.out.println(key+ "--" + data.get(key));
		}
	}
	
}