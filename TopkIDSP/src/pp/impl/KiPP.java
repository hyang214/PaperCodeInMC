package pp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import pp.PostProcess;
import tools.NaivePatternExtract;
import util.NaivePattern;
import util.Pattern;
import util.PeerPattern;

/**
 * author: Hao 
 * date:Feb 17, 2016
 * time:4:05:31 PM
 * purpose:
 */
public class KiPP implements PostProcess<PeerPattern, NaivePattern> {

	private Collection<PeerPattern> input;
	private Collection<NaivePattern> output;
	
	
	@Override
	public void inputResult(Collection<PeerPattern> input) {
		this.input = input;
		postProcess();
	}

	@Override
	public Collection<NaivePattern> outputResult() {
		return output;
	}

	private void postProcess() {
		output = new ArrayList<>();
		for(PeerPattern peer : input){
			for(Pattern p : peer.getPeerlist()){
				System.out.println("PP:" + p.toString());
				List<NaivePattern> e = NaivePatternExtract.INSTANCE.extract(p);
				for(NaivePattern np : e){
					System.out.println(np);
				}
				output.addAll(NaivePatternExtract.INSTANCE.extract(p));
				
			}
		}
	}

	@Override
	public void print() {
		System.out.println("Post processed result:");
		for(NaivePattern np : output){
			System.out.println(np);
		}
	}
}
