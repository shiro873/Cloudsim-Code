import java.util.ArrayList;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

/**
 * CloudletCreator Creates Cloudlets as per the User Requirements.
 * More Long tasks then short tasks which makes min-min outperform max-min
 *
 */
public class CloudletCreator2 {
	
	
	//cloudlet creator
	public ArrayList<Cloudlet> createUserCloudlet(int reqTasks,int brokerId){
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		
    	//Cloudlet properties
    	int id = 0;
    	int pesNumber=1;
    	long[] length = {3000, 2000, 3000, 3000, 3500, 2500, 2500, 2200, 3200, 2500};
    	long fileSize = 300;
    	long outputSize = 300;
    	UtilizationModel utilizationModel = new UtilizationModelFull();
    	   	
    	
    	for(id=0;id<reqTasks;id++){
    		
    		Cloudlet task = new Cloudlet(id, length[id], pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    		task.setUserId(brokerId);
    		
    		
    		//System.out.println("Task"+id+"="+(task.getCloudletLength()));
    		
    		//add the cloudlets to the list
        	cloudletList.add(task);
    	}

    	System.out.println("SUCCESSFULLY Cloudletlist created :)");

		return cloudletList;
		
	}

}