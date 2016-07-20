package com.example.devel.evermeal.Widget.listviewfeed.data;

public class FeedItem {
	private int aid, type;
	private String content, dependency, image_url, upload_date, uploader, name;
	private Boolean textCenter;

	public FeedItem() {
	}

	public int getAid() {
		return aid;
	}
	public void setAid(int id) {
		this.aid = id;
	}


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getDependency()
	{
		return dependency;
	}

	public void setDependency(String dependency)
	{
		this.dependency = dependency;
	}

	public String getImage_url()
	{
		return image_url;
	}

	public void setImage_url(String image_url)
	{
		this.image_url = image_url;
	}

	public String getUpload_date()
	{
		return upload_date;
	}

	public void setUpload_date(String upload_date)
	{
		this.upload_date = upload_date;
	}

	public String getUploader()
	{
		return uploader;
	}

	public void setUploader(String uploader)
	{
		this.uploader = uploader;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Boolean getTextCenter()
	{
		return textCenter;
	}

	public void setTextCenter(Boolean textCenter)
	{
		this.textCenter = textCenter;
	}
}
