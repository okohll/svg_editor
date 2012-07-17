package libraries;

import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import sun.nio.cs.*;

/**
 * the class of a uri encoder
 */
public class URIEncoderDecoder {

	/**
	 * Encodes all characters >= \u0080 into escaped, normalized UTF-8 octets,
	 * assuming that s is otherwise legal
	 * @param s a string
	 * @return the encoded string
	 */ 
	public static String encode(String s) {
		int n = s.length();
		if (n == 0)
			return s;

		// First check whether we actually need to encode
		for (int i = 0;;) {
			if (s.charAt(i) >= '\u0080' || s.charAt(i)==' ')
				break;
			if (++i >= n)
				return s;
		}

		String ns =s;//Normalizer.normalize(s, Normalizer.COMPOSE, 0);
		ByteBuffer bb = null;
		try {
			bb = ThreadLocalCoders.encoderFor("UTF-8")
			.encode(CharBuffer.wrap(ns));
		} catch (CharacterCodingException x) {//TODO
			x.printStackTrace();
			assert false;
		}

		StringBuffer sb = new StringBuffer();

		if(bb!=null){

			while (bb.hasRemaining()) {
				int b = bb.get() & 0xff;
				if (b >= 0x80 || b==0x20)
					appendEscape(sb, (byte)b);
				else
					sb.append((char)b);
			}
		}

		return sb.toString();
	}

	// -- Escaping and encoding --

	private final static char[] hexDigits = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};

	private static void appendEscape(StringBuffer sb, byte b) {
		sb.append('%');
		sb.append(hexDigits[(b >> 4) & 0x0f]);
		sb.append(hexDigits[(b >> 0) & 0x0f]);
	}

	/** Evaluates all escapes in s, applying UTF-8 decoding if needed.  Assumes
	 * that escapes are well-formed syntactically, i.e., of the form %XX.  If a
	 * sequence of escaped octets is not valid UTF-8 then the erroneous octets
	 * are replaced with '\uFFFD'.
	 * @param s the string to decode
	 * @return the decoded string
	 */
	public static String decode(String s) {

		if (s == null)
			return s;
		int n = s.length();
		if (n == 0)
			return s;
		if (s.indexOf('%') < 0)
			return s;

		StringBuffer sb = new StringBuffer(n);
		ByteBuffer bb = ByteBuffer.allocate(n);
		CharBuffer cb = CharBuffer.allocate(n);
		CharsetDecoder dec = ThreadLocalCoders.decoderFor("UTF-8")
		.onMalformedInput(CodingErrorAction.REPLACE)
		.onUnmappableCharacter(CodingErrorAction.REPLACE);

		// This is not horribly efficient, but it will do for now
		char c = s.charAt(0);
		boolean betweenBrackets = false;

		for (int i = 0; i < n;) {
			assert c == s.charAt(i);	// Loop invariant
			if (c == '[') {
				betweenBrackets = true;
			} else if (betweenBrackets && c == ']') {
				betweenBrackets = false;
			}
			if (c != '%' || betweenBrackets) {
				sb.append(c);
				if (++i >= n)
					break;
				c = s.charAt(i);
				continue;
			}
			bb.clear();

			for (;;) {
				assert (n - i >= 2);
				bb.put(decode(s.charAt(++i), s.charAt(++i)));
				if (++i >= n)
					break;
				c = s.charAt(i);
				if (c != '%')
					break;
			}
			bb.flip();
			cb.clear();
			dec.reset();
			CoderResult cr = dec.decode(bb, cb, true);
			assert cr.isUnderflow();
			cr = dec.flush(cb);
			assert cr.isUnderflow();
			sb.append(cb.flip().toString());
		}

		return sb.toString();
	}

	private static int decode(char c) {
		if ((c >= '0') && (c <= '9'))
			return c - '0';
		if ((c >= 'a') && (c <= 'f'))
			return c - 'a' + 10;
		if ((c >= 'A') && (c <= 'F'))
			return c - 'A' + 10;
		assert false;
		return -1;
	}

	private static byte decode(char c1, char c2) {
		return (byte)(  ((decode(c1) & 0xf) << 4)
				| ((decode(c2) & 0xf) << 0));
	}
	
	/**
	 * relatives the provided child URI to the provided base URI
	 * @param baseURI the base URI
	 * @param childURI the child URI
	 * @return the relatived URI
	 */
	public static URI relative(URI baseURI, URI childURI){
		
		URI relURI=childURI;
		
		if(baseURI!=null && childURI!=null){
			
			try{
				String basePath=baseURI.toASCIIString();
				String childPath=childURI.toASCIIString();
				
				String relPath="";
				int pos=childPath.indexOf(basePath);
				
				if(pos!=-1){
					
					relPath=childPath.substring(basePath.length(), childPath.length());
					
					if(relPath!=null && relPath.length()>0){
						
						relURI=new URI(relPath);
					}
				}
			}catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}
		
		return relURI;
	}
	
	   public static String HTMLEntityEncode( String s )
	   {
	       StringBuffer buf = new StringBuffer();
	       for ( int i = 0; i < s.length(); i++ )
	       {
	           char c = s.charAt( i );
	           if ( c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' )
	           {
	               buf.append( c );
	           }
	           else
	           {
	               buf.append( "&#" + (int)c + ";" );
	           }
	       }
	       return buf.toString();
	   }
}

