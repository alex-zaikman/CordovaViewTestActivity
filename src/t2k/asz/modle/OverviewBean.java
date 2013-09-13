package t2k.asz.modle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OverviewBean {



	private int classId;
	private String courseId;
	private String title;
	private String overview;
	private String cid;
	
	private Item root;


	public OverviewBean(int classId,  Map<String, Object> raw) {
		super();
		this.classId = classId;



		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) raw.get("data");

		this.setTitle((String) data.get("title"));

		this.setCid((String) data.get("cid"));

		
		@SuppressWarnings("unchecked")
		Map<String, Object> toc = (Map<String, Object>) data.get("toc");

		this.courseId = (String) raw.get("id");

		this.root = new Item();

		root.setTitle((String) toc.get("title"));

		root.setOverview((String) toc .get("overview"));

		root.setCid( (String) toc.get("cid"));

		root.setLeaf(false);
		
		root.setExpandded(true);
		
		parceStructure(this.root, toc);
	}

	private void parceStructure(Item root ,  Map<String, Object> data){

	
		List<Map<String, Object>> tocItems = (List<Map<String, Object>>) data.get("tocItems");

		for (Map<String, Object>  tocitem : tocItems){

			Item node = new Item();

			node.setTitle((String) tocitem.get("title"));

			node.setOverview((String)tocitem.get("overview"));

			node.setCid((String)tocitem.get("cid"));

			node.setLeaf(false);
			
			node.setExpandded(true);
			
			root.addChiled(node);

			parceStructure(node, tocitem);

		}


		List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

		for (Map<String, Object>  item : items){


			Item node = new Item();

			node.setTitle((String) item.get("title"));

			node.setOverview((String)item.get("description"));

			node.setCid((String)item.get("cid"));

			node.setLeaf(true);
			
			node.setExpandded(true);
			
			root.addChiled(node);

		}


	}


	public List<Item> listExpanddedItemsByDFS(){
		List<Item> ret = new  LinkedList<Item>();
		listExpanddedItemsByDFS( this.root ,ret);
		return ret;
	}

	private void listExpanddedItemsByDFS( Item node ,List<Item> list){
		if(node.isExpandded()){
			list.add(node);
			//if(!node.isLeaf()){
				for(Item chiled : node.getChildren()){
					listExpanddedItemsByDFS( chiled ,list);
				}
		//	}
		}
	}



	public class Item{

		private String title;
		private String overview;
		private String cid;

		private boolean leaf;
		private boolean expandded;
		private Item perent;
		private List<Item> children;

		public Item(){

			expandded=true;
			perent=null;
			children = new LinkedList<Item>();
		}

		public void addChiled(Item chiled){
			this.children.add(chiled);
		}
		public String getCid() {
			return cid;
		}
		public void setCid(String cid) {
			this.cid = cid;
		}
		public String getOverview() {
			return overview;
		}
		public void setOverview(String overview) {
			this.overview = overview;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public boolean isLeaf() {
			return leaf;
		}
		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		public boolean isExpandded() {
			return expandded;
		}
		public void setExpandded(boolean expandded) {
			this.expandded = expandded;
		}
		public Item getPerent() {
			return perent;
		}
		public void setPerent(Item perent) {
			this.perent = perent;
		}
		public List<Item> getChildren() {
			return children;
		}
		public void setChildren(List<Item> children) {
			this.children = children;
		}
		public void toggleExpand(){
			this.expandded = !this.isExpandded();
		}

	}


	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Item getRoot() {
		return root;
	}
	public void setRoot(Item root) {
		this.root = root;
	}

}
