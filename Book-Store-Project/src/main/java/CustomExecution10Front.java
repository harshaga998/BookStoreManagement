
import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.spi.DirStateFactory.Result;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class CustomExecution10Front extends Thread {
	static Map<Integer, Long> threadIdNdStartTimeMap = new HashMap<Integer, Long>();
	static Map<Integer, Long> threadIdNdEndTimeMap = new HashMap<Integer, Long>();
	static int threadName = 0;
	static int counter = 0;
	static int errorResponse = 0;
	static int sequence = 0;
	
	static boolean last = false;

	public static String getcompiledJsonDataUrl(StringBuilder rawName) {
		rawName = rawName.replace(0, 16, "");
		rawName = rawName.replace(rawName.length() - 4, rawName.length(), "");
		String googleCompiledCodeStoragePath = "https://storage.googleapis.com/myanatomy-dev/compile/stage/";
		String compiledJsonDataUrl = googleCompiledCodeStoragePath + rawName + ".json";
		return compiledJsonDataUrl;
	}

	public static void writeCompiledJsonData(String compiledJsonDataUrl) throws IOException {
		OutputStream logs = new FileOutputStream(new File("/tmp/logs.txt"), true);
		String g = "";
		Long customRequestTime = 10000l;
		Long startTime = System.currentTimeMillis();
		try {
			URL obj = new URL(compiledJsonDataUrl);
			HttpURLConnection con = null;
			int responseCode = 0;
			while ((System.currentTimeMillis() - startTime) <= customRequestTime) {
				con = (HttpURLConnection) obj.openConnection();
				con.connect();
				responseCode = con.getResponseCode();
				if (responseCode == 200) {
					sequence++;
					g = sequence + "|" + (System.currentTimeMillis() - startTime) + "\n";
					logs.write(g.getBytes());
					// System.out.println("Data found in "+ (System.currentTimeMillis()-startTime) +
					// " msecs ");
					break;
				}
				// Enable to set ping interval to 1 second (also add catch statement).
				// Thread.sleep(1000l);
			}
			if (responseCode != 200) {
				g = "\nUNABLE TO FIND JSON FILE\n";
				logs.write(g.getBytes());
				logs.close();
				// System.out.println("\nUNABLE TO FIND JSON FILE\n");
				return;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			// g = "\nJSON DATA: "+ sb;
			// logs.write(g.getBytes());
			// System.out.println("\nJSON DATA: "+ sb);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logs.close();
	}

	public void run() {
		executeTestCase(false);
	}

	private static Result executeTestCase(boolean intLast) {
		System.out.println("started1" + intLast);
		try {
			String POST_URL = "https://apimap.mymapit.in/prod/ajax/candidate/customExecution";
			String USER_AGENT = "Mozilla/5.0";
			String cookies = "STATNINJA_SESSION=\"a232cc88ef3189a91081151c3aedbb728e764123-id=816&username=nvfig%40nsiv&name=Hvgeqniq&role=Candidate&unRegisterUser=true\"";
			URL obj = new URL(POST_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");// PUT or POST
			con.setRequestProperty("Cookie", cookies);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/json");
			String input = "{\"code\":\"fact = [1]\\r\\nmod = 10**9 + 7\\r\\ndef factorial(n):\\r\\n    global  mod\\r\\n    global  fact\\r\\n    fact = [1]\\r\\n    mod = 10**9 + 7\\r\\n    for i in range(1,n+1):\\r\\n        fact.append(fact[-1] * i)\\r\\n        fact[-1] %= mod\\r\\ndef C(n,k):\\r\\n    global mod\\r\\n    return  (((fact[n]*pow(fact[k],mod-2,mod))%mod)*pow(fact[n-k],mod-2,mod))%mod\\r\\ndef good(x,a,b):\\r\\n    while x > 0:\\r\\n        if(x%10!=a and x%10!=b):\\r\\n            return  False\\r\\n        x//=10\\r\\n    return  True\\r\\nfor _ in range(int(input())):\\r\\n    ans = 0\\r\\n    a,b,n = map(int,input().split())\\r\\n    factorial(n)\\r\\n    for i in range(n+1):\\r\\n        if(good(i*a+(n-i)*b,a,b)):\\r\\n            ans += C(n,i)\\r\\n            ans %= mod\\r\\n    print(ans)\",\"customTestCase\":[{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"},{\"input\":\"1\\n1 2 2\",\"output\":\"1\"}],\"language\":\"PYTHON3\",\"executionType\":\"ExecuteWithCustomTestCases\",\"type\":\"DEFAULT\"}";
			OutputStream os = con.getOutputStream();
			os.write(input.getBytes());
			con.connect();
			int responseCode = con.getResponseCode();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			//threadName = Integer.parseInt(Thread.currentThread().getName());
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			if (intLast) {
				//String compiledJsonDataUrl = getcompiledJsonDataUrl(sb);
				//writeCompiledJsonData(compiledJsonDataUrl);
			}
			br.close();
			//threadIdNdEndTimeMap.put(Integer.parseInt(Thread.currentThread().getName()), System.currentTimeMillis());
			if (responseCode == 200) {
				if (sb.toString().contains("SYSTEM_ERROR")) {
					// System.out.println("SYSTEM ERROR OCCURED for Thread : "+threadName+" |
					// Response : "+responseCode+" | Time :
					// "+(threadIdNdEndTimeMap.get(threadName)-threadIdNdStartTimeMap.get(threadName)));
					counter++;
				} else {
					// System.out.println("Success for Thread : "+threadName+" | Response :
					// "+responseCode+" | Time :
					// "+(threadIdNdEndTimeMap.get(threadName)-threadIdNdStartTimeMap.get(threadName)));
				}
			} else {
				// System.out.println("******************** OTHER THAN 200 RESPONSE GOT
				// ************************************* : "+responseCode);
				errorResponse++;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static class MyRunnable implements Runnable{

		int noOfRequests;
	    public MyRunnable(int noOfRequests) {
	    	this.noOfRequests = noOfRequests;
		}

		@Override
	    public void run() {
			ExecutorService exec = Executors.newCachedThreadPool();
			List<Callable<Result>> tasks = new ArrayList<Callable<Result>>();
			for (int k = 0; k < noOfRequests; k++) {
				last = false;
				if (k == noOfRequests - 1) {
					last = true;
				}
				// Runnable task = null;
				Callable<Result> c = new Callable<Result>() {
					@Override
					public Result call() throws Exception {
						return executeTestCase(last);
						// runTestCasesAtOnce(noOfRequests, delayTime, threadList, logs);
					}
				};
				tasks.add(c);
			}
			try {
				System.out.println("ss");
				exec.invokeAll(tasks);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				System.out.println("ss2");
				e.printStackTrace();
			}
	    }
	    
	}
	public static void main(String args[]) throws IOException {
		// Total number of requests
		int noOfRequests = Integer.parseInt(args[0]);
		String g = "";
		// delayTime in seconds
		Long delayTime = Long.parseLong(args[1]);
		// No. of times to repeat.
		int repeatCount = Integer.parseInt(args[2]);
		Long startingTime = System.currentTimeMillis();
		//List<Thread> threadList = new ArrayList<>();
		OutputStream logs = new FileOutputStream(new File("/tmp/logs.txt"), true);
		
		Thread t1= null;
		for (int i =0; i<repeatCount; i++) {
			t1 = new Thread(new MyRunnable(noOfRequests), "Thread-" +i);
			t1.start();
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
        
        //let all threads finish execution before finishing main thread
        try {
            t1.join();
            System.exit(0);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		logs.close();
		return;
	}
}