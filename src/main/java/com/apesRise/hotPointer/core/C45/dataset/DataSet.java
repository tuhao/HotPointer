package com.apesRise.hotPointer.core.C45.dataset;

/**
 * A data set wrapping all the data.
 */
public class DataSet {
	/** The name of the data set **/
	protected String name;
	/** The specified classes **/
	protected String[] classes;
	/** The array of attributes wrapping their attribute values. **/
	private Attribute[] attributes;
	/** The index of the class attribute in the attribute array **/
	private int classAttributeIndex;
	private int casecount = 0;

	private String[] attributeNames;

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(String[] attributeNames) {
		this.attributeNames = attributeNames;
	}

	/**
	 * Initialize a data set
	 * 
	 * @param baseName
	 *            The base name of the input files (.names and .data)
	 */
	public DataSet(String baseName) {
		int beginIndex = baseName.lastIndexOf("/");
		if (beginIndex < 0) {
			beginIndex = baseName.lastIndexOf("\\");
		}
		this.name = baseName.substring(beginIndex + 1);
	}

	/**
	 * Set the name of the data set
	 * 
	 * @param name
	 *            The name of the data set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the data set
	 * 
	 * @return the name of the data set
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the attributes of the data set
	 * 
	 * @param attributes
	 *            The attributes of the data set
	 */
	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
		this.classAttributeIndex = attributes.length - 1;
	}

	/**
	 * Get the attributes of the data set
	 * 
	 * @return The attributes of the data set
	 */
	public Attribute[] getAttributes() {
		return attributes;
	}

	/**
	 * Get the index of the class attribute
	 * 
	 * @return The class attribute index
	 */
	public int getClassAttributeIndex() {
		return classAttributeIndex;
	}

	/**
	 * Set the index of the class attribute
	 * 
	 * @param classAttributeIndex
	 *            The index of the class attribute
	 */
	public void setClassAttributeIndex(int classAttributeIndex) {
		this.classAttributeIndex = classAttributeIndex;
	}

	/**
	 * Get the number of the data in the data set
	 * 
	 * @return The number of the data in the data set
	 */
	public int getCaseCount() {
		return this.casecount;
	}

	public void addCaseCount() {
		this.casecount++;
	}

	/**
	 * Get the number of the attributes in the data set
	 * 
	 * @return The number of the attributes in the data set
	 */
	public int getAttributeCount() {
		return attributes.length;
	}

	/**
	 * Sets the specified classes of the data set
	 * 
	 * @param classes
	 *            The classes of the data set
	 */
	public void setClassValues(String[] classes) {
		this.classes = classes;
	}

	/**
	 * Get the nominal values of the class attribute
	 * 
	 * @return The nominal values of the class attribute
	 */
	public String[] getClassValues() {
		return this.classes;
	}

	/**
	 * Get the number of different class values
	 * 
	 * @param The
	 *            number of different class values
	 */
	public int getClassCount() {
		return this.classes.length;
	}

}