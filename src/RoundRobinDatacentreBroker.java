

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

public class RoundRobinDatacentreBroker extends DatacenterBroker {

	public RoundRobinDatacentreBroker(String name) throws Exception {
		super(name);
	}
	
	@Override
	protected void processResourceCharacteristics(SimEvent ev) {
		DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
		getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

		if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
			createVmsInDatacenter(getDatacenterIdsList());
		}
	};
	
	protected void createVmsInDatacenter(List<Integer> datacenterIds) {
		
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		int i = 0;
		for (Vm vm : getVmList()) {
			
			int datacenterId = datacenterIds.get(i++ % datacenterIds.size());
			String datacenterName = CloudSim.getEntityName(datacenterId);
			
			if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
				Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
				sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
				requestedVms++;
			}
		}

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	};
}
