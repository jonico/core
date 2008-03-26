package trial2;

import org.openadaptor.auxil.processor.simplerecord.FilterProcessor;

public class FilterTransformerWriter {
	
	FilterProcessor Filter;
	Object transformer;
	Object Writer;
	
	public FilterTransformerWriter(FilterProcessor filter, Object transformer,
			Object writer) {
		super();
		Filter = filter;
		this.transformer = transformer;
		Writer = writer;
	}

	public FilterProcessor getFilter() {
		return Filter;
	}

	public void setFilter(FilterProcessor filter) {
		Filter = filter;
	}

	public Object getTransformer() {
		return transformer;
	}

	public void setTransformer(Object transformer) {
		this.transformer = transformer;
	}

	public Object getWriter() {
		return Writer;
	}

	public void setWriter(Object writer) {
		Writer = writer;
	}
}
