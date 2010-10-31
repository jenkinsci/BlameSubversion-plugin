package hudson.notify;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.logging.Logger;

import org.kohsuke.stapler.DataBoundConstructor;

/***
 * 
 * @author tang
 *
 */

public class UpDownStreamNumberSychronizedNotify extends Notifier {
	protected static final Logger LOGGER = Logger.getLogger(UpDownStreamNumberSychronizedNotify.class.getName());
	
	private String times;
	private int itimes=1;
	
    public String getTimes() {
		return times;
	}


	public void setTimes(String times) {
		this.times = times;
	}

	@DataBoundConstructor
	public UpDownStreamNumberSychronizedNotify(String times) {
		this.times = times;
		try{
			itimes = Integer.parseInt(times);
		}catch (Exception e) {
		}
	}
	
	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;	
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		try{
			BuildNumberSychronize buildNumberSychronize=null;
			switch(itimes){
			  case 1:
				  buildNumberSychronize= new SameBuildNumberSychronize(build,listener);
			      break;
			  case 10:
				  buildNumberSychronize = new TenTimesBuildNumberSychronize(build,listener);
			      break;
			  case 100:
				  buildNumberSychronize = new HundredTimesBuildNumberSychronize(build,listener);
			      break;
			   default: 
				  buildNumberSychronize= new SameBuildNumberSychronize(build,listener);
				  break;
			}
			 buildNumberSychronize.sychronizeBuildNumber();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the descriptor value.
	 * 
	 * @return the descriptor value.
	 */
	@Override
	public BuildNumberSychonizeDescriptorImpl getDescriptor() {
		return (BuildNumberSychonizeDescriptorImpl) super.getDescriptor();
	}
	
	
	 @Extension
	 public static final class BuildNumberSychonizeDescriptorImpl extends BuildStepDescriptor<Publisher> {
		    private int times;
		    public BuildNumberSychonizeDescriptorImpl() {
	            super(UpDownStreamNumberSychronizedNotify.class);
	        }

	        public String getDisplayName() {
	            return "UpDownStreamNumberSychronizedNotify";
	        }
	        
//	        @Override
//			public boolean configure(StaplerRequest req, JSONObject formData)
//					throws FormException {
//				req.bindJSON(this, formData);
//				save();
//
//				return super.configure(req, formData);
//			}

	        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
	            return true;
	        }
	        
	        public int getTimes() {
	    		return times;
	    	}

	    	public void setTimes(int times) {
	    		this.times = times;
	    	}
	    	public FormValidation doTimes(){
	    		return FormValidation.ok();
	    	}
	}

}
