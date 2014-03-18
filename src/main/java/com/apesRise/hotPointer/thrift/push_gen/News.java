package com.apesRise.hotPointer.thrift.push_gen;

/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class News implements org.apache.thrift.TBase<News, News._Fields>, java.io.Serializable, Cloneable, Comparable<News> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("News");

  private static final org.apache.thrift.protocol.TField TITLE_FIELD_DESC = new org.apache.thrift.protocol.TField("title", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField ARTICLES_FIELD_DESC = new org.apache.thrift.protocol.TField("articles", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new NewsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new NewsTupleSchemeFactory());
  }

  public String title; // required
  public List<Article> articles; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TITLE((short)1, "title"),
    ARTICLES((short)2, "articles");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TITLE
          return TITLE;
        case 2: // ARTICLES
          return ARTICLES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TITLE, new org.apache.thrift.meta_data.FieldMetaData("title", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ARTICLES, new org.apache.thrift.meta_data.FieldMetaData("articles", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Article.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(News.class, metaDataMap);
  }

  public News() {
  }

  public News(
    String title,
    List<Article> articles)
  {
    this();
    this.title = title;
    this.articles = articles;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public News(News other) {
    if (other.isSetTitle()) {
      this.title = other.title;
    }
    if (other.isSetArticles()) {
      List<Article> __this__articles = new ArrayList<Article>(other.articles.size());
      for (Article other_element : other.articles) {
        __this__articles.add(new Article(other_element));
      }
      this.articles = __this__articles;
    }
  }

  public News deepCopy() {
    return new News(this);
  }

  @Override
  public void clear() {
    this.title = null;
    this.articles = null;
  }

  public String getTitle() {
    return this.title;
  }

  public News setTitle(String title) {
    this.title = title;
    return this;
  }

  public void unsetTitle() {
    this.title = null;
  }

  /** Returns true if field title is set (has been assigned a value) and false otherwise */
  public boolean isSetTitle() {
    return this.title != null;
  }

  public void setTitleIsSet(boolean value) {
    if (!value) {
      this.title = null;
    }
  }

  public int getArticlesSize() {
    return (this.articles == null) ? 0 : this.articles.size();
  }

  public java.util.Iterator<Article> getArticlesIterator() {
    return (this.articles == null) ? null : this.articles.iterator();
  }

  public void addToArticles(Article elem) {
    if (this.articles == null) {
      this.articles = new ArrayList<Article>();
    }
    this.articles.add(elem);
  }

  public List<Article> getArticles() {
    return this.articles;
  }

  public News setArticles(List<Article> articles) {
    this.articles = articles;
    return this;
  }

  public void unsetArticles() {
    this.articles = null;
  }

  /** Returns true if field articles is set (has been assigned a value) and false otherwise */
  public boolean isSetArticles() {
    return this.articles != null;
  }

  public void setArticlesIsSet(boolean value) {
    if (!value) {
      this.articles = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TITLE:
      if (value == null) {
        unsetTitle();
      } else {
        setTitle((String)value);
      }
      break;

    case ARTICLES:
      if (value == null) {
        unsetArticles();
      } else {
        setArticles((List<Article>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TITLE:
      return getTitle();

    case ARTICLES:
      return getArticles();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TITLE:
      return isSetTitle();
    case ARTICLES:
      return isSetArticles();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof News)
      return this.equals((News)that);
    return false;
  }

  public boolean equals(News that) {
    if (that == null)
      return false;

    boolean this_present_title = true && this.isSetTitle();
    boolean that_present_title = true && that.isSetTitle();
    if (this_present_title || that_present_title) {
      if (!(this_present_title && that_present_title))
        return false;
      if (!this.title.equals(that.title))
        return false;
    }

    boolean this_present_articles = true && this.isSetArticles();
    boolean that_present_articles = true && that.isSetArticles();
    if (this_present_articles || that_present_articles) {
      if (!(this_present_articles && that_present_articles))
        return false;
      if (!this.articles.equals(that.articles))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(News other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetTitle()).compareTo(other.isSetTitle());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTitle()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.title, other.title);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetArticles()).compareTo(other.isSetArticles());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetArticles()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.articles, other.articles);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("News(");
    boolean first = true;

    sb.append("title:");
    if (this.title == null) {
      sb.append("null");
    } else {
      sb.append(this.title);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("articles:");
    if (this.articles == null) {
      sb.append("null");
    } else {
      sb.append(this.articles);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class NewsStandardSchemeFactory implements SchemeFactory {
    public NewsStandardScheme getScheme() {
      return new NewsStandardScheme();
    }
  }

  private static class NewsStandardScheme extends StandardScheme<News> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, News struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TITLE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.title = iprot.readString();
              struct.setTitleIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ARTICLES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.articles = new ArrayList<Article>(_list0.size);
                for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                {
                  Article _elem2;
                  _elem2 = new Article();
                  _elem2.read(iprot);
                  struct.articles.add(_elem2);
                }
                iprot.readListEnd();
              }
              struct.setArticlesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, News struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.title != null) {
        oprot.writeFieldBegin(TITLE_FIELD_DESC);
        oprot.writeString(struct.title);
        oprot.writeFieldEnd();
      }
      if (struct.articles != null) {
        oprot.writeFieldBegin(ARTICLES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.articles.size()));
          for (Article _iter3 : struct.articles)
          {
            _iter3.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NewsTupleSchemeFactory implements SchemeFactory {
    public NewsTupleScheme getScheme() {
      return new NewsTupleScheme();
    }
  }

  private static class NewsTupleScheme extends TupleScheme<News> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, News struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTitle()) {
        optionals.set(0);
      }
      if (struct.isSetArticles()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetTitle()) {
        oprot.writeString(struct.title);
      }
      if (struct.isSetArticles()) {
        {
          oprot.writeI32(struct.articles.size());
          for (Article _iter4 : struct.articles)
          {
            _iter4.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, News struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.title = iprot.readString();
        struct.setTitleIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.articles = new ArrayList<Article>(_list5.size);
          for (int _i6 = 0; _i6 < _list5.size; ++_i6)
          {
            Article _elem7;
            _elem7 = new Article();
            _elem7.read(iprot);
            struct.articles.add(_elem7);
          }
        }
        struct.setArticlesIsSet(true);
      }
    }
  }

}

