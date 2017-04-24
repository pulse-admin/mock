package gov.ca.emsa;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Util {
	private static final Logger logger = LogManager.getLogger(Util.class);
	
	private List<String> pids;
	
	public Util() {
		pids = new ArrayList<String>();
	}

	public List<String> getStatuses(String patientIds) {
		String[] patientIdsArr = patientIds.split(",");
		for(int i = 0; i < patientIdsArr.length; i++) {
			pids.add(patientIdsArr[i]);
		}
		return pids;
	}

}