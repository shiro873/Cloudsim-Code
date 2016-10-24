

import java.util.ArrayList;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class SelectiveBroker extends DatacenterBroker {

	public SelectiveBroker(String name) throws Exception {
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
    		
		}
		
		for (Vm vm : getVmList()) {
    		vlist.add(vm);
    		
		}

		
		double completionTime[][] = new double[reqTasks][reqVms];
		double execTime[][] = new double[reqTasks][reqVms];
		double time =0.0;
		
		for(int i=0; i<reqTasks; i++){
			for(int j=0; j<reqVms; j++){
				time = getCompletionTime(clist.get(i), vlist.get(j));
				completionTime[i][j]= time;
				time = getExecTime(clist.get(i), vlist.get(j));
				execTime[i][j]= time;
				
				System.out.print(execTime[i][j]+" ");
				
			}
			System.out.println();
			
		}
		
		int minCloudlet=0, maxCloudlet=0;
		int minVm=0;
		double min=-1.0d;
		
		for(int c=0; c< clist.size(); c++){
			
			for(int i=0;i<clist.size();i++){
				for(int j=0;j<(vlist.size()-1);j++){
					if(completionTime[i][j+1] > completionTime[i][j] && completionTime[i][j+1] > -1.0){
						minCloudlet=i;
					}
				}
			}
			
			
				for(int j=0; j<vlist.size(); j++){
					time = getExecTime(clist.get(minCloudlet), vlist.get(j));
					if(time < min && time > -1.0){
						minVm=j;
						min=time;
					}
			}
				
			double totalCompletionTime=0.0;
			double avgCompletionTime=0.0;
			double sd=0.0;
			double p = 0.0;
			
			for(int i=0;i<clist.size();i++){
				totalCompletionTime += completionTime[i][minVm];
			}
			
			avgCompletionTime = totalCompletionTime/reqTasks;
			
			double temp = completionTime[minCloudlet][minVm] - avgCompletionTime;
			double temp2 = temp * temp;
			double temp3 = temp2/reqTasks;
			sd = Math.sqrt(temp3);
			
			
			
			if(p <= reqTasks/2 || sd < vlist.get(minVm).getMips()){
				for(int i=0;i<clist.size();i++){
					for(int j=0;j<(vlist.size()-1);j++){
						if(completionTime[i][j+1] < completionTime[i][j] && completionTime[i][j+1] > -1.0){
							maxCloudlet=i;
						}
					}
				}
				
				bindCloudletToVm(maxCloudlet, minVm);
				clist.remove(minCloudlet);
			}
			else{
				for(int i=0;i<clist.size();i++){
					for(int j=0;j<(vlist.size()-1);j++){
						if(completionTime[i][j+1] > completionTime[i][j] && completionTime[i][j+1] > -1.0){
							minCloudlet=i;
						}
					}
				}
				bindCloudletToVm(maxCloudlet, minVm);
				clist.remove(maxCloudlet);
			}
			
			for(int i=0; i<clist.size(); i++){
				double t= diff(completionTime[i][minVm],completionTime[i+1][minVm]);
				if(t>sd){
					p=t;
				}
			}
			
			
			
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
	
	private double getExecTime(Cloudlet cloudlet, Vm vm){
		return cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
	}
	
	private double diff(double t1, double t2){
		return t1-t2;
	}
}
