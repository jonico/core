/*******************************************************************************
 * Code taken from the Feed and Read Project in SourceForge
 * 
 * Copyright notice                                                            *
 *                                                                             *
 * Copyright (c) 2005 Feed'n Read Development Team                             *
 * http://sourceforge.net/fnr                                                  *
 *                                                                             *
 * All rights reserved.                                                        *
 *                                                                             *
 * This program and the accompanying materials are made available under the    *
 * terms of the Common Public License v1.0 which accompanies this distribution,*
 * and is available at                                                         *
 * http://www.eclipse.org/legal/cpl-v10.html                                   *
 *                                                                             *
 * A copy is found in the file cpl-v10.html and important notices to the       *
 * license from the team is found in the textfile LICENSE.txt distributed      *
 * in this package.                                                            *
 *                                                                             *
 * This copyright notice MUST APPEAR in all copies of the file.                *
 *                                                                             *
 * Contributors:                                                               *
 *    Feed'n Read - initial API and implementation                             *
 *                  (smachhau@users.sourceforge.net)                           *
 *******************************************************************************/

package com.collabnet.ccf.core.utils;


import java.util.HashMap;
import java.util.Map;



/**
 * <p>
 * Contains <code>String</code> related utility methods.
 * </p>
 * 
 * @author <a href="mailto:smachhau@users.sourceforge.net">Sebastian Machhausen</a>
 */
@SuppressWarnings("unchecked")
public class StringUtils {

	
	/**
	 * <p>
	 * The longest an entity can be {@value #LONGEST_ENTITY}, including the
	 * lead & and trail ;
	 * </p>
	 * 
	 * @see #SHORTEST_ENTITY
	 */
	private final static int LONGEST_ENTITY = 10; /* &thetasym; */

	/**
	 * <p>
	 * The shortest an entity can be {@value #SHORTEST_ENTITY}, including the
	 * lead &amp; and trailing ;
	 * </p>
	 * 
	 * @see #LONGEST_ENTITY
	 */
    private final static int SHORTEST_ENTITY = 4; /* &#1; &lt; */

    /**
	 * <p>
	 * Maps entity names to their corresponding unicode <code>Characters</code>.
	 * </p>
	 */
	private static Map<String, Character> ENTITY_MAPPING;
	
	private static Map<Character, String> CHAR_ENTITY_MAPPING;
	
	/**
	 * <p>
	 * Regular expression pattern to match any HTML tags.
	 * </p>
	 * 
	 * @see #stripHTML(String)
	 * @see #convertHTML(String)
	 */
	private final static String HTML_PATTERN = "(<[^>]+>)";
	
	private final static String BR_PATTERN = "(<[bB][rR][/]?>)";
	
	private final static String SLASH_N_PATTERN = "(\n)";
    
	private final static String UNDERSCORE_PATTERN = "________________________________________";
	
    /** <p> Constant for the empty <code>String</code> </p> */
    public final static String EMPTY_STRING = "";
    
    /** <p> Constant for the line separator <p> */
    public final static String LINE_SEPARATOR = 
        System.getProperty("line.separator");
    
    /** <p> The default delimiter char to use </p> */
    public final static char DEFAULT_DELIMITER = ',';
    
	
	/**
	 * <p>
	 * Static initialization block builds the internal entity mapping table.
	 * </p>
	 */
	static {
		String[] entityKeys = { "quot"
		/* 34 */, "amp"
		/* 38 */, "lt"
		/* 60 */, "gt"
		/* 62 */, "nbsp"
		/* 160 */, "iexcl"
		/* 161 */, "cent"
		/* 162 */, "pound"
		/* 163 */, "curren"
		/* 164 */, "yen"
		/* 165 */, "brvbar"
		/* 166 */, "sect"
		/* 167 */, "uml"
		/* 168 */, "copy"
		/* 169 */, "ordf"
		/* 170 */, "laquo"
		/* 171 */, "not"
		/* 172 */, "shy"
		/* 173 */, "reg"
		/* 174 */, "macr"
		/* 175 */, "deg"
		/* 176 */, "plusmn"
		/* 177 */, "sup2"
		/* 178 */, "sup3"
		/* 179 */, "acute"
		/* 180 */, "micro"
		/* 181 */, "para"
		/* 182 */, "middot"
		/* 183 */, "cedil"
		/* 184 */, "sup1"
		/* 185 */, "ordm"
		/* 186 */, "raquo"
		/* 187 */, "frac14"
		/* 188 */, "frac12"
		/* 189 */, "frac34"
		/* 190 */, "iquest"
		/* 191 */, "Agrave"
		/* 192 */, "Aacute"
		/* 193 */, "Acirc"
		/* 194 */, "Atilde"
		/* 195 */, "Auml"
		/* 196 */, "Aring"
		/* 197 */, "AElig"
		/* 198 */, "Ccedil"
		/* 199 */, "Egrave"
		/* 200 */, "Eacute"
		/* 201 */, "Ecirc"
		/* 202 */, "Euml"
		/* 203 */, "Igrave"
		/* 204 */, "Iacute"
		/* 205 */, "Icirc"
		/* 206 */, "Iuml"
		/* 207 */, "ETH"
		/* 208 */, "Ntilde"
		/* 209 */, "Ograve"
		/* 210 */, "Oacute"
		/* 211 */, "Ocirc"
		/* 212 */, "Otilde"
		/* 213 */, "Ouml"
		/* 214 */, "times"
		/* 215 */, "Oslash"
		/* 216 */, "Ugrave"
		/* 217 */, "Uacute"
		/* 218 */, "Ucirc"
		/* 219 */, "Uuml"
		/* 220 */, "Yacute"
		/* 221 */, "THORN"
		/* 222 */, "szlig"
		/* 223 */, "agrave"
		/* 224 */, "aacute"
		/* 225 */, "acirc"
		/* 226 */, "atilde"
		/* 227 */, "auml"
		/* 228 */, "aring"
		/* 229 */, "aelig"
		/* 230 */, "ccedil"
		/* 231 */, "egrave"
		/* 232 */, "eacute"
		/* 233 */, "ecirc"
		/* 234 */, "euml"
		/* 235 */, "igrave"
		/* 236 */, "iacute"
		/* 237 */, "icirc"
		/* 238 */, "iuml"
		/* 239 */, "eth"
		/* 240 */, "ntilde"
		/* 241 */, "ograve"
		/* 242 */, "oacute"
		/* 243 */, "ocirc"
		/* 244 */, "otilde"
		/* 245 */, "ouml"
		/* 246 */, "divide"
		/* 247 */, "oslash"
		/* 248 */, "ugrave"
		/* 249 */, "uacute"
		/* 250 */, "ucirc"
		/* 251 */, "uuml"
		/* 252 */, "yacute"
		/* 253 */, "thorn"
		/* 254 */, "yuml"
		/* 255 */, "OElig"
		/* 338 */, "oelig"
		/* 339 */, "Scaron"
		/* 352 */, "scaron"
		/* 353 */, "Yuml"
		/* 376 */, "fnof"
		/* 402 */, "circ"
		/* 710 */, "tilde"
		/* 732 */, "Alpha"
		/* 913 */, "Beta"
		/* 914 */, "Gamma"
		/* 915 */, "Delta"
		/* 916 */, "Epsilon"
		/* 917 */, "Zeta"
		/* 918 */, "Eta"
		/* 919 */, "Theta"
		/* 920 */, "Iota"
		/* 921 */, "Kappa"
		/* 922 */, "Lambda"
		/* 923 */, "Mu"
		/* 924 */, "Nu"
		/* 925 */, "Xi"
		/* 926 */, "Omicron"
		/* 927 */, "Pi"
		/* 928 */, "Rho"
		/* 929 */, "Sigma"
		/* 931 */, "Tau"
		/* 932 */, "Upsilon"
		/* 933 */, "Phi"
		/* 934 */, "Chi"
		/* 935 */, "Psi"
		/* 936 */, "Omega"
		/* 937 */, "alpha"
		/* 945 */, "beta"
		/* 946 */, "gamma"
		/* 947 */, "delta"
		/* 948 */, "epsilon"
		/* 949 */, "zeta"
		/* 950 */, "eta"
		/* 951 */, "theta"
		/* 952 */, "iota"
		/* 953 */, "kappa"
		/* 954 */, "lambda"
		/* 955 */, "mu"
		/* 956 */, "nu"
		/* 957 */, "xi"
		/* 958 */, "omicron"
		/* 959 */, "pi"
		/* 960 */, "rho"
		/* 961 */, "sigmaf"
		/* 962 */, "sigma"
		/* 963 */, "tau"
		/* 964 */, "upsilon"
		/* 965 */, "phi"
		/* 966 */, "chi"
		/* 967 */, "psi"
		/* 968 */, "omega"
		/* 969 */, "thetasym"
		/* 977 */, "upsih"
		/* 978 */, "piv"
		/* 982 */, "ensp"
		/* 8194 */, "emsp"
		/* 8195 */, "thinsp"
		/* 8201 */, "zwnj"
		/* 8204 */, "zwj"
		/* 8205 */, "lrm"
		/* 8206 */, "rlm"
		/* 8207 */, "ndash"
		/* 8211 */, "mdash"
		/* 8212 */, "lsquo"
		/* 8216 */, "rsquo"
		/* 8217 */, "sbquo"
		/* 8218 */, "ldquo"
		/* 8220 */, "rdquo"
		/* 8221 */, "bdquo"
		/* 8222 */, "dagger"
		/* 8224 */, "Dagger"
		/* 8225 */, "bull"
		/* 8226 */, "hellip"
		/* 8230 */, "permil"
		/* 8240 */, "prime"
		/* 8242 */, "Prime"
		/* 8243 */, "lsaquo"
		/* 8249 */, "rsaquo"
		/* 8250 */, "oline"
		/* 8254 */, "frasl"
		/* 8260 */, "euro"
		/* 8364 */, "image"
		/* 8465 */, "weierp"
		/* 8472 */, "real"
		/* 8476 */, "trade"
		/* 8482 */, "alefsym"
		/* 8501 */, "larr"
		/* 8592 */, "uarr"
		/* 8593 */, "rarr"
		/* 8594 */, "darr"
		/* 8595 */, "harr"
		/* 8596 */, "crarr"
		/* 8629 */, "lArr"
		/* 8656 */, "uArr"
		/* 8657 */, "rArr"
		/* 8658 */, "dArr"
		/* 8659 */, "hArr"
		/* 8660 */, "forall"
		/* 8704 */, "part"
		/* 8706 */, "exist"
		/* 8707 */, "empty"
		/* 8709 */, "nabla"
		/* 8711 */, "isin"
		/* 8712 */, "notin"
		/* 8713 */, "ni"
		/* 8715 */, "prod"
		/* 8719 */, "sum"
		/* 8721 */, "minus"
		/* 8722 */, "lowast"
		/* 8727 */, "radic"
		/* 8730 */, "prop"
		/* 8733 */, "infin"
		/* 8734 */, "ang"
		/* 8736 */, "and"
		/* 8743 */, "or"
		/* 8744 */, "cap"
		/* 8745 */, "cup"
		/* 8746 */, "int"
		/* 8747 */, "there4"
		/* 8756 */, "sim"
		/* 8764 */, "cong"
		/* 8773 */, "asymp"
		/* 8776 */, "ne"
		/* 8800 */, "equiv"
		/* 8801 */, "le"
		/* 8804 */, "ge"
		/* 8805 */, "sub"
		/* 8834 */, "sup"
		/* 8835 */, "nsub"
		/* 8836 */, "sube"
		/* 8838 */, "supe"
		/* 8839 */, "oplus"
		/* 8853 */, "otimes"
		/* 8855 */, "perp"
		/* 8869 */, "sdot"
		/* 8901 */, "lceil"
		/* 8968 */, "rceil"
		/* 8969 */, "lfloor"
		/* 8970 */, "rfloor"
		/* 8971 */, "lang"
		/* 9001 */, "rang"
		/* 9002 */, "loz"
		/* 9674 */, "spades"
		/* 9824 */, "clubs"
		/* 9827 */, "hearts"
		/* 9829 */, "diams"
		/* 9830 */, };
				
		char[] entityValues = { 34
		/* &quot; */, 38
		/* &amp; */, 60
		/* &lt; */, 62
		/* &gt; */, 160
		/* &nbsp; */, 161
		/* &iexcl; */, 162
		/* &cent; */, 163
		/* &pound; */, 164
		/* &curren; */, 165
		/* &yen; */, 166
		/* &brvbar; */, 167
		/* &sect; */, 168
		/* &uml; */, 169
		/* &copy; */, 170
		/* &ordf; */, 171
		/* &laquo; */, 172
		/* &not; */, 173
		/* &shy; */, 174
		/* &reg; */, 175
		/* &macr; */, 176
		/* &deg; */, 177
		/* &plusmn; */, 178
		/* &sup2; */, 179
		/* &sup3; */, 180
		/* &acute; */, 181
		/* &micro; */, 182
		/* &para; */, 183
		/* &middot; */, 184
		/* &cedil; */, 185
		/* &sup1; */, 186
		/* &ordm; */, 187
		/* &raquo; */, 188
		/* &frac14; */, 189
		/* &frac12; */, 190
		/* &frac34; */, 191
		/* &iquest; */, 192
		/* &Agrave; */, 193
		/* &Aacute; */, 194
		/* &Acirc; */, 195
		/* &Atilde; */, 196
		/* &Auml; */, 197
		/* &Aring; */, 198
		/* &AElig; */, 199
		/* &Ccedil; */, 200
		/* &Egrave; */, 201
		/* &Eacute; */, 202
		/* &Ecirc; */, 203
		/* &Euml; */, 204
		/* &Igrave; */, 205
		/* &Iacute; */, 206
		/* &Icirc; */, 207
		/* &Iuml; */, 208
		/* &ETH; */, 209
		/* &Ntilde; */, 210
		/* &Ograve; */, 211
		/* &Oacute; */, 212
		/* &Ocirc; */, 213
		/* &Otilde; */, 214
		/* &Ouml; */, 215
		/* &times; */, 216
		/* &Oslash; */, 217
		/* &Ugrave; */, 218
		/* &Uacute; */, 219
		/* &Ucirc; */, 220
		/* &Uuml; */, 221
		/* &Yacute; */, 222
		/* &THORN; */, 223
		/* &szlig; */, 224
		/* &agrave; */, 225
		/* &aacute; */, 226
		/* &acirc; */, 227
		/* &atilde; */, 228
		/* &auml; */, 229
		/* &aring; */, 230
		/* &aelig; */, 231
		/* &ccedil; */, 232
		/* &egrave; */, 233
		/* &eacute; */, 234
		/* &ecirc; */, 235
		/* &euml; */, 236
		/* &igrave; */, 237
		/* &iacute; */, 238
		/* &icirc; */, 239
		/* &iuml; */, 240
		/* &eth; */, 241
		/* &ntilde; */, 242
		/* &ograve; */, 243
		/* &oacute; */, 244
		/* &ocirc; */, 245
		/* &otilde; */, 246
		/* &ouml; */, 247
		/* &divide; */, 248
		/* &oslash; */, 249
		/* &ugrave; */, 250
		/* &uacute; */, 251
		/* &ucirc; */, 252
		/* &uuml; */, 253
		/* &yacute; */, 254
		/* &thorn; */, 255
		/* &yuml; */, 338
		/* &OElig; */, 339
		/* &oelig; */, 352
		/* &Scaron; */, 353
		/* &scaron; */, 376
		/* &Yuml; */, 402
		/* &fnof; */, 710
		/* &circ; */, 732
		/* &tilde; */, 913
		/* &Alpha; */, 914
		/* &Beta; */, 915
		/* &Gamma; */, 916
		/* &Delta; */, 917
		/* &Epsilon; */, 918
		/* &Zeta; */, 919
		/* &Eta; */, 920
		/* &Theta; */, 921
		/* &Iota; */, 922
		/* &Kappa; */, 923
		/* &Lambda; */, 924
		/* &Mu; */, 925
		/* &Nu; */, 926
		/* &Xi; */, 927
		/* &Omicron; */, 928
		/* &Pi; */, 929
		/* &Rho; */, 931
		/* &Sigma; */, 932
		/* &Tau; */, 933
		/* &Upsilon; */, 934
		/* &Phi; */, 935
		/* &Chi; */, 936
		/* &Psi; */, 937
		/* &Omega; */, 945
		/* &alpha; */, 946
		/* &beta; */, 947
		/* &gamma; */, 948
		/* &delta; */, 949
		/* &epsilon; */, 950
		/* &zeta; */, 951
		/* &eta; */, 952
		/* &theta; */, 953
		/* &iota; */, 954
		/* &kappa; */, 955
		/* &lambda; */, 956
		/* &mu; */, 957
		/* &nu; */, 958
		/* &xi; */, 959
		/* &omicron; */, 960
		/* &pi; */, 961
		/* &rho; */, 962
		/* &sigmaf; */, 963
		/* &sigma; */, 964
		/* &tau; */, 965
		/* &upsilon; */, 966
		/* &phi; */, 967
		/* &chi; */, 968
		/* &psi; */, 969
		/* &omega; */, 977
		/* &thetasym; */, 978
		/* &upsih; */, 982
		/* &piv; */, 8194
		/* &ensp; */, 8195
		/* &emsp; */, 8201
		/* &thinsp; */, 8204
		/* &zwnj; */, 8205
		/* &zwj; */, 8206
		/* &lrm; */, 8207
		/* &rlm; */, 8211
		/* &ndash; */, 8212
		/* &mdash; */, 8216
		/* &lsquo; */, 8217
		/* &rsquo; */, 8218
		/* &sbquo; */, 8220
		/* &ldquo; */, 8221
		/* &rdquo; */, 8222
		/* &bdquo; */, 8224
		/* &dagger; */, 8225
		/* &Dagger; */, 8226
		/* &bull; */, 8230
		/* &hellip; */, 8240
		/* &permil; */, 8242
		/* &prime; */, 8243
		/* &Prime; */, 8249
		/* &lsaquo; */, 8250
		/* &rsaquo; */, 8254
		/* &oline; */, 8260
		/* &frasl; */, 8364
		/* &euro; */, 8465
		/* &image; */, 8472
		/* &weierp; */, 8476
		/* &real; */, 8482
		/* &trade; */, 8501
		/* &alefsym; */, 8592
		/* &larr; */, 8593
		/* &uarr; */, 8594
		/* &rarr; */, 8595
		/* &darr; */, 8596
		/* &harr; */, 8629
		/* &crarr; */, 8656
		/* &lArr; */, 8657
		/* &uArr; */, 8658
		/* &rArr; */, 8659
		/* &dArr; */, 8660
		/* &hArr; */, 8704
		/* &forall; */, 8706
		/* &part; */, 8707
		/* &exist; */, 8709
		/* &empty; */, 8711
		/* &nabla; */, 8712
		/* &isin; */, 8713
		/* &notin; */, 8715
		/* &ni; */, 8719
		/* &prod; */, 8721
		/* &sum; */, 8722
		/* &minus; */, 8727
		/* &lowast; */, 8730
		/* &radic; */, 8733
		/* &prop; */, 8734
		/* &infin; */, 8736
		/* &ang; */, 8743
		/* &and; */, 8744
		/* &or; */, 8745
		/* &cap; */, 8746
		/* &cup; */, 8747
		/* &int; */, 8756
		/* &there4; */, 8764
		/* &sim; */, 8773
		/* &cong; */, 8776
		/* &asymp; */, 8800
		/* &ne; */, 8801
		/* &equiv; */, 8804
		/* &le; */, 8805
		/* &ge; */, 8834
		/* &sub; */, 8835
		/* &sup; */, 8836
		/* &nsub; */, 8838
		/* &sube; */, 8839
		/* &supe; */, 8853
		/* &oplus; */, 8855
		/* &otimes; */, 8869
		/* &perp; */, 8901
		/* &sdot; */, 8968
		/* &lceil; */, 8969
		/* &rceil; */, 8970
		/* &lfloor; */, 8971
		/* &rfloor; */, 9001
		/* &lang; */, 9002
		/* &rang; */, 9674
		/* &loz; */, 9824
		/* &spades; */, 9827
		/* &clubs; */, 9829
		/* &hearts; */, 9830
		/* &diams; */, };
				
		ENTITY_MAPPING = new HashMap<String, Character>(511);
		CHAR_ENTITY_MAPPING = new HashMap<Character, String>(511);
		for (int i = 0; i < entityKeys.length; i++) {
			ENTITY_MAPPING.put(entityKeys[i], new Character(entityValues[i]));
			CHAR_ENTITY_MAPPING.put(new Character(entityValues[i]),entityKeys[i]);
		}
	} // end static initialization block

	
    /**
	 * <p>
	 * Converts the specified <code>entity</code> to a single
	 * <code>char</code>.
	 * </p>
	 * 
	 * @param entity the entity to convert; it must have lead &amp; and trail ;
	 * stripped; may be a x#123 or #123 style entity; works faster if the entity
	 * is specified in lower case
	 * 
	 * @return the equivalent character; <i>0</i> if the entity is unknown
	 */
    public static char entityToChar(String entity) {
		Character code = (Character) ENTITY_MAPPING.get(entity);
		if (code != null) {
			return (code.charValue());
		}		
		code = (Character) ENTITY_MAPPING.get(entity.toLowerCase());
		if (code != null) {
			return (code.charValue());
		}		
		/* Check at least having &#1; */
		if (entity.length() < 2) {
			return (0);
		}
		
		try {
			switch (entity.charAt(0)) {
			case 'x':
			case 'X':
				/* Handle entities denoted in hexadecimal */
				if (entity.charAt(1) != '#') {
					return (0);
				}
				/* Ensure at least having &x#1; */
				if (entity.length() < 3) {
					return (0);
				}
				return (char) Integer.parseInt(entity.substring(2), 16);
			case '#':
				/* Handle decimal entities */
				return ((char) Integer.parseInt(entity.substring(1)));
			default:
				return (0);
			}
		} catch (NumberFormatException e) {
			return (0);
		}
	} // end method entityToChar(String)

    
    /**
	 * <p>
	 * Converts HTML to text converting entities such as &quot; back to " and
	 * &lt; back to &lt; Ordinary text passes unchanged.
	 * </p>
	 * 
	 * @param text raw text to be processed; must not be <code>null</code>
	 * 
	 * @return converted text; HTML 4.0 entities such as &hearts; &#123; and
	 * &x#123; &nbsp; -> 16 are also handled; <code>null</code> input also
	 * returns <code>null</code>
	 */
    public static String convertEntities(String text) {
		if (text == null) {
			return (null);
		}
		if (text.indexOf('&') < 0) {
			/* There are no entities, nothing to do */
			return (text);
		}
		int originalTextLength = text.length();
		StringBuffer sb = new StringBuffer(originalTextLength);
		for (int i = 0; i < originalTextLength; i++) {
			int whereAmp = text.indexOf('&', i);
			if (whereAmp < 0) {
				/* no more &s, we are done: Append all remaining text */
				sb.append(text.substring(i));
				break;
			} else {
				/* Append all text to left of next & */
				sb.append(text.substring(i, whereAmp));
				/* Avoid reprocessing those chars */
				i = whereAmp;
				/* text.charAt(i) is an & possEntity has lead & stripped. */
				String possEntity = text.substring(i + 1, Math.min(i
						+ LONGEST_ENTITY, text.length()));
				char t = potentialEntityToChar(possEntity);
				if (t != 0) {
					/* It was a good entity, keep its equivalent char. */
					sb.append(t);
					/* Avoid reprocessing chars forming the entity */
					int whereSemi = possEntity
							.indexOf(";", SHORTEST_ENTITY - 2);
					i += whereSemi + 1;
				} else {
					/* Treat & just as ordinary character */
					sb.append('&');
				}
			} 
		}
		/* If result is not shorter, we did not do anything. Saves RAM. */
		return (sb.length() == originalTextLength) ? text : sb.toString();
	} // end method convertEntities(String)
    
    public static String encodeHTMLToEntityReferences(String html){
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i < html.length(); i++){
    		char c = html.charAt(i);
    		String entity = CHAR_ENTITY_MAPPING.get(c);
    		if(entity != null){
    			sb.append('&');
    			sb.append(entity);
    			sb.append(';');
    		}
    		else {
    			sb.append(c);
    		}
    	}
    	String newStr = sb.toString();
    	String newStrWithSlashN = newStr.replaceAll(SLASH_N_PATTERN, "<br>"); 
    	return newStrWithSlashN;
    }
    
    
    /**
	 * <p>
	 * Strips off all HTML tags from the specified <code>text</code>.
	 * </p>
	 * 
	 * @param text the raw text to strip HTML tags from
	 * 
	 * @return the text with all HTML tags stripped of; if <code>null</code>
	 * is specified <code>null</code> is also returned
	 */
    public static String stripHTML(String text) {
    	if (text != null) {
    		text = text.replaceAll(BR_PATTERN, "\n");
    		text = text.replaceAll(HTML_PATTERN, EMPTY_STRING);
    		return stripSpecificDelimiter(text);
    	} else {
    		return (null);
    	}
    } // end method stripHTML(String)
    
    /**
	 * <p>
	 * Strips off all Underscore tags from the specified <code>text</code>.
	 * </p>
	 * 
	 * @param text the raw text to strip Underscore tags from
	 * 
	 * @return the text with all Underscore tags stripped of; if <code>null</code>
	 * is specified <code>null</code> is also returned
	 */
    public static String stripSpecificDelimiter(String text) {
    	if (text != null) {
    		return (text.replaceAll(UNDERSCORE_PATTERN, EMPTY_STRING));
    	} else {
    		return (null);
    	}
    } // end method stripHTML(String)
    
    
    /**
	 * <p>
	 * Converts the specified HTML input <code>String</code> to pure raw text.
	 * All HTML entities are converted and all HTML tags stripped off.
	 * </p>
	 * 
	 * @param text the HTML input <code>String</code> to convert; a
	 * <code>null</code> value is converted to an empty <code>String</code>
	 * @return the converted raw text
	 * 
	 * @see #convertEntities(String)
	 * @see #stripHTML(String)
	 */
    public static String convertHTML(String text) {
    	text = nullValueToEmptyString(text);
    	text = convertEntities(text);
    	text = stripHTML(text);
    	
    	return (text);
    } // end method convertHTML(String)
    
    
    /**
	 * <p>
	 * Converts a possible <code>null</code> String object to an empty
	 * <code>String</code> object. If the specified value is non null the
	 * value is just being trimmed using {@link java.lang.String#trim}
	 * </p>
	 * 
	 * @param str the <code>String</code> object to convert
	 * 
	 * @return an empty <code>String</code> if the specified value is set to
	 * <code>null</code>; otherwise the specified value is being trimmed
	 * using {@link java.lang.String#trim}
	 */
    public static String nullValueToEmptyString(String str) {
       return (str == null ? EMPTY_STRING : str.trim());
   } // end method nullValueToEmptyString(String)

    
   /**
	 * <p>
	 * Tests if the given <code>String</code> is empty. A <code>String</code>
	 * object is considered empty if it is either <code>null</code> or its
	 * trimmed length is zero.
	 * </p>
	 * 
	 * @param str the <code>String</code> to test
	 * @return <code>true</code> if the given <code>String</code> is empty;
	 * <code>false</code> otherwise
	 */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    } // end method isEmpty(String)
    
    
    /**
	 * <p>
	 * Checks a number of gauntlet conditions to ensure this is a valid entity.
	 * Converts the entity to the corresponding char.
	 * </p>
	 * 
	 * @param entity <code>String</code> that may hold an entity; lead & must
	 * be stripped, but may contain text past the ;
	 * 
	 * @return corresponding unicode character, or <i>0</i> if the entity is
	 * invalid
	 */
    private static char potentialEntityToChar(String entity) {
		if (entity.length() < SHORTEST_ENTITY - 1) {
			return (0);
		}

		/* Find the trailing ; */
		int whereSemi = entity.indexOf(';', SHORTEST_ENTITY - 2); 			
		if (whereSemi < SHORTEST_ENTITY - 2) {
			return (0);
		}

		/*
		 * we found a potential entity, at least it has &xxxx; lead & already
		 * stripped, now strip trailing ; and look it up in a table. Will return
		 * 0 for an invalid entity.
		 */
		return (entityToChar(entity.substring(0, whereSemi)));
	} // end method potentialEntityToChar(String)
 
    
} // end class StringUtils