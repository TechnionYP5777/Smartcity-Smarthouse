/**
 * 
 */
package il.ac.technion.cs.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import il.ac.technion.cs.simulation.Simulation.AcuAction;
import il.ac.technion.cs.smarthouse.sensors.PathType;

class AcuState {
		private final String id;
		private Integer currTemp, requestedTemp, naturalRoomTemp;
		private AcuAction action;
		
		public AcuState(String id, Integer naturalRoomTemp){
			this.id = id;
			this.naturalRoomTemp = this.requestedTemp = naturalRoomTemp;
			this.currTemp = naturalRoomTemp -10;//*(ThreadLocalRandom.current().nextBoolean()? 1:-1);
			this.action = AcuAction.STOP;
		}
		
		private synchronized Integer getWantedTemp(){
			return action.isOn() ? requestedTemp : naturalRoomTemp;
		}
		
		private synchronized Map<String, Object> getCurrentState(Boolean calcFuture){
			Map<String, Object> data = new HashMap<>();
			data.put(Simulation.getPath(PathType.INFO_SENDING,Simulation.tempSuffix), currTemp);
			data.put(Simulation.getPath(PathType.INFO_SENDING,Simulation.stateSuffix), action.isOn());

			if(calcFuture){
				if(action.isOn())
					currTemp += (AcuAction.HOTTER.equals(action)? +1 : -1);
				else{ //if(ThreadLocalRandom.current().nextBoolean())
					Integer delta = currTemp - getWantedTemp();
					delta = delta.equals(0) ? delta :delta/Math.abs(delta);
					currTemp -= delta;
				}
			
			}
			
			return data;
		}
		
		public synchronized Map<String, Object> getCurrentState(){
			return getCurrentState(true);
		}
		
		public synchronized Boolean processInstruction(String path, String val){
			Supplier<Map<String,Object>> getm = ()->{
				Map<String,Object> m = new HashMap<>();
				m.put("deftemp", requestedTemp);
				m.put("action", action);
				return m;
			};
			Map<String,Object> old = getm.get();
			if(Simulation.getPath(PathType.INSTRUCTION_RECEIVING,Simulation.stateSuffix).equals(path))
				action = AcuAction.valueOf(val);
			if(Simulation.getPath(PathType.INSTRUCTION_RECEIVING,Simulation.defaultTempSuffix).equals(path))
//				defaultTemp = Integer.parseInt(val);
				requestedTemp =   Double.valueOf(Double.parseDouble(val)).intValue();
			Map<String,Object> curr = getm.get();
			
			System.out.println(String.join("\n", 
							"id:"+id+" received \tinst:"+path+"\tval:"+val
							,
							"old:\n"+old+"\nnew:\n"+curr +"\n"
							));
			return true;
		}

		/**
		 * @return
		 */
		public String getId() {
			 return id;
		}
	}