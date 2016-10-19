

import java.util.ArrayList;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * 
 *
 */
public class EnhancedMaxMinBroker extends DatacenterBroker {

	public EnhancedMaxMinBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	
	
	public void scheduleTaskstoVms(){
		int reqTasks= cloudletList.size();
		int reqVms= vmList.size();
		
		
		ArrayList<Cloudlet> clist = new ArrayList<Cloudlet>();
		ArrayList<Vm> vlist = new ArrayList<Vm>();
		
		for (Cloudlet cloudlet : getCloudletList()) {
    		clist.add(cloudlet);
    		//System.out.println("clist:" +clist.get(k).getCloudletId());
    		
		}
		
		for (Vm vm : getVmList()) {
    		vlist.add(vm);
    		//System.out.println("vlist:" +vlist.get(k).getId());
    		
		}

		
		double completionTime[][] = new double[reqTasks][reqVms];
		double time =0.0;
		double avgTime= 0.0, totalTime=0.0;
		
		for(int i=0; i<reqTasks; i++){
			for(int j=0; j<reqVms; j++){
				time = getCompletionTime(clist.get(i), vlist.get(j));
				completionTime[i][j]= time;
				//System.out.println("Completion Time: "+ completionTime[i][j]);
				totalTime=totalTime+time;
			}
		}
		
		avgTime=totalTime/(reqTasks+reqTasks);
		
		int maxCloudlet=0, avgCloudlet=0;
		int minVm=0;
		double max=-1.0d;
		
		for(int i=0;i<clist.size();i++){
			for(int j=0;j<(vlist.size()-1);j++){
				if(completionTime[i][j+1] <= completionTime[i][j] && completionTime[i][j+1] > -1.0){
					minVm=j;
				}
			}
		}
		
		for(int i=0; i<clist.size(); i++){
			for(int j=0; j<vlist.size(); j++){
				time = getCompletionTime(clist.get(i), vlist.get(j));
				if(time >= avgTime && time < max){
					avgCloudlet=i;
				}
			}
		}
		
		bindCloudletToVm(avgCloudlet, minVm);
		clist.remove(avgCloudlet);
		
		for(int i=0; i<vlist.size(); i++){
			completionTime[avgCloudlet][i]=-1.0;
		}
		
		
		
		for(int c=0; c< clist.size(); c++){
			
			for(int i=0;i<clist.size();i++){
				for(int j=0;j<(vlist.size()-1);j++){
					if(completionTime[i][j+1] <= completionTime[i][j] && completionTime[i][j+1] > -1.0){
						minVm=j;
					}
				}
			}
			
			for(int i=0; i<clist.size(); i++){
				for(int j=0; j<vlist.size(); j++){
					time = getCompletionTime(clist.get(i), vlist.get(j));
					if(time < max && time > -1.0){
						maxCloudlet=i;
					}
				}
			}
			
			bindCloudletToVm(maxCloudlet, minVm);
			clist.remove(maxCloudlet);
			
			for(int i=0; i<vlist.size(); i++){
				completionTime[maxCloudlet][i]=-1.0;
			}
			
		}
		
		
	}	
	
	
	private double getCompletionTime(Cloudlet cloudlet, Vm vm){
		double waitingTime = cloudlet.getWaitingTime();
		double execTime = cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
		
		double completionTime = execTime + waitingTime;
		
		return completionTime;
	}
}