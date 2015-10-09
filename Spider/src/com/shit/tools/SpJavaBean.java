package com.shit.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpJavaBean
{
	static String domainPath = "src/com/shit/domain";
	static String returnSpace = "\n";
	static String pkgName = "package com.shit.domain;";
	static String importString = "import org.bson.Document;\nimport com.shit.tools.DomainValueFormater;";
	static String extendString = "Document";
	static boolean isUseSerialVersionUID = true;
	
	public String generate(String className, String[] members)
	{
		String result = "";
		String generateInfoString = "";
		String geterSetterString = "";
		String constructMethodString = "";
		String staticParse = "";
		String sserialVersionUID = "";
		String tmpImportStr = importString;

		generateInfoString += "\t/** " + returnSpace + "\t * @generate" + returnSpace;
		constructMethodString += returnSpace + "\tpublic " + className + "(){}" + returnSpace + "\tpublic " + className
				+ "(Document doc)" + returnSpace + "\t{" + returnSpace
				+ "\t\tfor(Entry<String, Object> kv : doc.entrySet())" + returnSpace + "\t\t{" + returnSpace
				+ "\t\t\tObject val = DomainValueFormater.formatValue(kv.getKey(), kv.getValue());" + returnSpace
				+ "\t\t\tthis.put(kv.getKey(), val);" + returnSpace + "\t\t}" + returnSpace + "\t}" + returnSpace;

		staticParse += returnSpace + "\tpublic static " + className + " parse(Document doc)" + returnSpace + "\t{"
				+ returnSpace + "\t\tif (doc != null)" + returnSpace + "\t\t\treturn new " + className + "(doc);"
				+ returnSpace + "\t\treturn null;" + returnSpace + "\t}" + returnSpace;

		String type = "";
		String pName = "";
		for (String s : members)
		{
			type = "";
			pName = "";
			s = s.replace(";", "");
			String[] tmp = s.split(" ");
			for (String ts : tmp)
			{
				if (ts.trim() != "")
				{
					if (type == "")
						type = ts;
					else if (pName == "")
						pName = ts;
					else
						throw new IllegalArgumentException("非法数据类型：" + s);
				}
			}

			String pNameUperFirst = (String.valueOf(pName.charAt(0))).toUpperCase() + pName.substring(1);

			generateInfoString += "\t * " + type + " " + pName + ";" + returnSpace;
			geterSetterString += returnSpace + "\tpublic " + type + " get" + pNameUperFirst + "()" + returnSpace + "\t{"
					+ returnSpace + "\t\treturn (" + type + ") this.get(\"" + pName + "\");" + returnSpace + "\t}"
					+ returnSpace + returnSpace + "\tpublic void set" + pNameUperFirst + "(" + type + " " + pName + ")"
					+ returnSpace + "\t{" + returnSpace + "\t\tthis.put(\"" + pName + "\", " + pName + ");"
					+ returnSpace + "\t}" + returnSpace;

			if (type.equals("Date") && !importString.contains("Date"))
			{
				tmpImportStr += returnSpace + "import java.util.Date;";
			}
		}

		generateInfoString += "\t */" + returnSpace;

		if (isUseSerialVersionUID)
			sserialVersionUID = returnSpace + "\tprivate static final long serialVersionUID = 1L;" + returnSpace;

		result += pkgName + returnSpace;
		result += returnSpace + tmpImportStr + returnSpace;
		result += returnSpace + "public class " + className + " extends " + extendString + returnSpace + "{"
				+ returnSpace;
		result += generateInfoString + sserialVersionUID + constructMethodString + geterSetterString + staticParse + "}"
				+ returnSpace;

		return result;
	}

	public String[] getMembers(String filePath)
	{
		String[] result = null;
		List<String> resultLst = new ArrayList<String>();
		String content = FileTools.readFromFile(filePath);
		content = content.substring(content.indexOf("@generate") + 9, content.indexOf("*/"));
		content = content.replace("*", "");
		String[] tmp = content.split(";");
		for (String s : tmp)
		{
			s = s.trim();
			if (!"".equals(s))
			{
				resultLst.add(s);
			}
		}

		result = new String[resultLst.size()];
		return resultLst.toArray(result);
	}

	public static void main(String[] args)
	{
		SpJavaBean spJavaBean = new SpJavaBean();

		File dir = new File(domainPath);
		for (File f : dir.listFiles())
		{
			String filePath = f.getAbsolutePath();
			String[] members = spJavaBean.getMembers(filePath);
			String className = f.getName();
			className = className.substring(0, className.lastIndexOf('.'));
			String content = spJavaBean.generate(className, members);
			FileTools.writeToFile(content, filePath, false);
		}
	}
}
