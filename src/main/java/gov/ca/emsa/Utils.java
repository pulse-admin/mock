package gov.ca.emsa;

import gov.ca.emsa.domain.Organization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<Organization> readOrganizations(InputStream file){
		List<Organization> orgs = new ArrayList<Organization>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(file));
		    String line = br.readLine();
		    while (line != null) {
		        orgs.add(new Organization(line));
		        line = br.readLine();
		    }
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orgs;
	}

}
