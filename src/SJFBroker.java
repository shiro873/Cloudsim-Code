import java.util.ArrayList;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class SJFBroker extends DatacenterBroker {

	public SJFBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void scheduleTaskstoVms(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		Vm vm = vmList.get(0);
		
		
		for(int i=0;i<reqTasks;i++){
    		bindCloudletToVm(i, (i%reqVms));
    		System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
    	}
		
		//System.out.println("reqTasks: "+ reqTasks);
		
		ArrayList<Cloudlet> list = new ArrayList<Cloudlet>();
		for (Cloudlet cloudlet : getCloudletReceivedList()) {
			list.add(cloudlet);
			
		}
		
		//setCloudletReceivedList(null);
		
		Cloudlet[] list2 = list.toArray(new Cloudlet[list.size()]);
		
		//System.out.println("size :"+list.size());
		
		Cloudlet temp= null;
		
		
		int n = list.size();
        
		
		for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                   
                    if(list2[j-1].getCloudletLength() / (vm.getMips()*vm.getNumberOfPes()) > list2[j].getCloudletLength() / (vm.getMips()*vm.getNumberOfPes())){
                            //swap the elements!
                            //swap(list2[j-1], list2[j]);
                    		temp= list2[j-1];
                    		list2[j-1]= list2[j];
                    		list2[j]=temp;
                    }
                 // printNumbers(list2);
            }
		}
		
		ArrayList<Cloudlet> list3 = new ArrayList<Cloudlet>();

		for(int i=0;i<list2.length;i++){
			list3.add(list2[i]);
		}
		//printNumbers(list);
		
		setCloudletReceivedList(list);
			    
		
		//System.out.println("\n\tSJFS Broker Schedules\n");
    	
		
    	
    	//System.out.println("\n");
	}
	
	
	
	public void printNumber(Cloudlet[] list){
		for(int i=0; i<list.length;i++){
			System.out.print(" "+ list[i].getCloudletId());
			System.out.println(list[i].getCloudletStatusString());
		}
		System.out.println();
	}
	public void printNumbers(ArrayList<Cloudlet> list){
		for(int i=0; i<list.size();i++){
			System.out.print(" "+ list.get(i).getCloudletId());
		}
		System.out.println();
	}
	
	@Override
	protected void processCloudletReturn(SimEvent ev) {
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		getCloudletReceivedList().add(cloudlet);
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloudlet " + cloudlet.getCloudletId()
		+ " received");
		cloudletsSubmitted--;
		if (getCloudletList().size() == 0 && cloudletsSubmitted == 0){
			scheduleTaskstoVms();
			cloudletExecution(cloudlet);
		}
	}
	
	
	protected void cloudletExecution(Cloudlet cloudlet){
		
		if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
			Log.printLine(CloudSim.clock() + ": " + getName() + ": All Cloudlets executed. Finishing...");
			clearDatacenters();
			finishExecution();
		} else { // some cloudlets haven't finished yet
			if (getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
				// all the cloudlets sent finished. It means that some bount
				// cloudlet is waiting its VM be created
				clearDatacenters();
				createVmsInDatacenter(0);
			}

		}
	}
	
	
}
