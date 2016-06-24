package com.example.im.util;
import java.util.Scanner;
public class MD5 {
	
	
	//ѹ������ÿ����ѭ����λ��
			static final int S11 = 7; 
	        static final int S12 = 12; 
	        static final int S13 = 17; 
	        static final int S14 = 22; 
	        static final int S21 = 5; 
	        static final int S22 = 9; 
	        static final int S23 = 14; 
	        static final int S24 = 20; 
	        static final int S31 = 4; 
	        static final int S32 = 11; 
	        static final int S33 = 16; 
	        static final int S34 = 23; 
	        static final int S41 = 6; 
	        static final int S42 = 10; 
	        static final int S43 = 15; 
	        static final int S44 = 21; 
	        
	        //��������,���е�һ����Ϊ-128������Ϊ-128�ڼ�����д洢Ϊ10000000
	        static final byte[] PADDING = { -128, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	        
	        private long[] register = new long[4];  // �Ĵ���ABCD
	        private long[] count = new long[2];  //�洢bit����mod 2^64��
	        private byte[] buffer = new byte[64]; //�洢���������
	         
	 		//hex������һ�μ�������16����ASCII��ʾ.  
	        public String hex; 
	         
	        // binary������һ�μ�������2�����ڲ���ʾ����ʾ128bit��MD5ֵ. 
	        private byte[] binary = new byte[16]; 
			 public MD5() { 
	            init(); 
	        }
	        
	        // init��һ����ʼ����������ʼ�����ı�����װ���׼�Ļ���  
	        private void init() { 
	                count[0] = 0L; 
	                count[1] = 0L; 
	                //�ĸ��Ĵ����ĳ�ʼֵ
	                register[0] = 0x67452301L; 
	                register[1] = 0xefcdab89L; 
	                register[2] = 0x98badcfeL; 
	                register[3] = 0x10325476L; 
	        }
	        
	        // F, G, H ,I ��4��������MD5����
	        private long F(long x, long y, long z) { 
	                return (x & y) | ((~x) & z); 
	        }
	        private long G(long x, long y, long z) { 
	                return (x & z) | (y & (~z)); 
	        }
	        private long H(long x, long y, long z) { 
	                return x ^ y ^ z; 
	        }
			 private long I(long x, long y, long z) { 
	                return y ^ (x | (~z)); 
	        }
			 
			//getMD5�Ǽ���MD5����Ҫ���̺�������ڲ�����Ҫ����MD5�任���ַ���,  ���ص��Ǳ任��Ľ�����������Ǵ�hex��ȡ�õ�
		        public String getMD5(String inbuf) { 
		                init(); 
		                calculation(inbuf.getBytes(), inbuf.length()); 
		                result(); 
		                hex = ""; 
		                for (int i = 0; i < 16; i++) { 
		                        hex += ByteToHEX(binary[i]); 
		                }
		                return hex; 
		        }
	          
	        //FF,GG,HH��II������F,G,H,I����ѹ�������ĵ���        
	        private long FF(long a, long b, long c, long d, long x, long s, long t) { 
	        	a += F (b, c, d) + x + t; 
	            a = ((int) a << s) | ((int) a >>> (32 - s)); 
	            a += b; 
	            return a;  
	        }
	        private long GG(long a, long b, long c, long d, long x, long s, long t){
	        	a += G (b, c, d) + x + t; 
	            a = ((int) a << s) | ((int) a >>> (32 - s)); 
	            a += b; 
	            return a; 
	        }
	        private long HH(long a, long b, long c, long d, long x, long s, long t){
	        	a += H (b, c, d) + x + t; 
	            a = ((int) a << s) | ((int) a >>> (32 - s)); 
	            a += b; 
	            return a;  
	        }
	        private long II(long a, long b, long c, long d, long x, long s, long t){
	        	a += I (b, c, d) + x + t; 
	            a = ((int) a << s) | ((int) a >>> (32 - s)); 
	            a += b; 
	            return a; 
	        }

	       //calculation��MD5����������̣�inbuf��Ҫ�任���ֽڴ���inputlen�ǳ��ȣ����������getMD5���� 
	        private void calculation(byte[] inbuf, int inputLen){ 
	                int i, index, partLen; 
	                byte[] temp = new byte[64]; 
	                index = (int)(count[0] >>> 3) & 0x3F; 
	                if ((count[0] += (inputLen << 3)) < (inputLen << 3))
						count[1]++; 
	                //System.out.println("????"+count[1]);
	                count[1] += (inputLen >>> 29); 
	                partLen = 64 - index; 
	                if (inputLen >= partLen){
	                	move(buffer, inbuf, index, 0, partLen); 
	                    transform(buffer); 
	                    for (i = partLen; i + 63 < inputLen; i += 64){
	                    	move(temp, inbuf, 0, i, 64); 
	                        transform (temp); 
	                    }
	                    index = 0; 
	                }
	                else
	                	i = 0; 
	                move(buffer, inbuf, index, i, inputLen - i);
	               // System.out.println("count="+count[0]);
	               // System.out.println("index="+index);
	        }
	        
	       //result�������д������ 
	        private void result () { 
	                byte[] bits = new byte[8]; 
	                int index, padLen;  
	                Encode (bits, count, 8); 
	                index = (int)((count[0] >>> 3) & 0x3f); 
	                padLen = (index < 56) ? (56 - index) : (120 - index); 
	              //  System.out.println("padlen="+padLen);
	                calculation (PADDING, padLen); 
	                //System.out.println("padding!!!!!!!!!!!");
	                calculation(bits, 8); 
	                //���Ĵ����е����洢��binary
	                Encode (binary, register, 16); 
	        } 
	          
	        //move��һ���ڲ�ʹ�õ�byte����Ŀ鿽����������input��inpos��ʼ��len���ȵ��ֽڸ��Ƶ�output����outposλ��Ϊ��ʼ�ĵط�  
	        private void move (byte[] output, byte[] input, int outpos, int inpos, int len){ 
			 int i; 
	                for (i = 0; i < len; i++) 
	                        output[outpos + i] = input[inpos + i]; 
	        }
	        
	        //transform��MD5��ѹ������
	        private void transform (byte temp[]) { 
	                long a = register[0], b = register[1], c = register[2], d = register[3]; 
	                long[] x = new long[16]; 
	                Decode (x, temp, 64); 
	                
	                //Round 1
	                a = FF (a, b, c, d, x[0], S11, 0xd76aa478L);
	                d = FF (d, a, b, c, x[1], S12, 0xe8c7b756L);
	                c = FF (c, d, a, b, x[2], S13, 0x242070dbL);
	                b = FF (b, c, d, a, x[3], S14, 0xc1bdceeeL); 
	                a = FF (a, b, c, d, x[4], S11, 0xf57c0fafL);
	                d = FF (d, a, b, c, x[5], S12, 0x4787c62aL);
	                c = FF (c, d, a, b, x[6], S13, 0xa8304613L); 
	                b = FF (b, c, d, a, x[7], S14, 0xfd469501L); 
	                a = FF (a, b, c, d, x[8], S11, 0x698098d8L);
	                d = FF (d, a, b, c, x[9], S12, 0x8b44f7afL); 
	                c = FF (c, d, a, b, x[10], S13, 0xffff5bb1L);
	                b = FF (b, c, d, a, x[11], S14, 0x895cd7beL);
	                a = FF (a, b, c, d, x[12], S11, 0x6b901122L);
	                d = FF (d, a, b, c, x[13], S12, 0xfd987193L);
	                c = FF (c, d, a, b, x[14], S13, 0xa679438eL);
	                b = FF (b, c, d, a, x[15], S14, 0x49b40821L);
					 // Round 2
	                a = GG (a, b, c, d, x[1], S21, 0xf61e2562L);
	                d = GG (d, a, b, c, x[6], S22, 0xc040b340L);
	                c = GG (c, d, a, b, x[11], S23, 0x265e5a51L);
	                b = GG (b, c, d, a, x[0], S24, 0xe9b6c7aaL);
	                a = GG (a, b, c, d, x[5], S21, 0xd62f105dL);
	                d = GG (d, a, b, c, x[10], S22, 0x2441453L);
	                c = GG (c, d, a, b, x[15], S23, 0xd8a1e681L);
	                b = GG (b, c, d, a, x[4], S24, 0xe7d3fbc8L);
	                a = GG (a, b, c, d, x[9], S21, 0x21e1cde6L);
	                d = GG (d, a, b, c, x[14], S22, 0xc33707d6L);
	                c = GG (c, d, a, b, x[3], S23, 0xf4d50d87L);
	                b = GG (b, c, d, a, x[8], S24, 0x455a14edL);
	                a = GG (a, b, c, d, x[13], S21, 0xa9e3e905L); 
	                d = GG (d, a, b, c, x[2], S22, 0xfcefa3f8L);
	                c = GG (c, d, a, b, x[7], S23, 0x676f02d9L);
	                b = GG (b, c, d, a, x[12], S24, 0x8d2a4c8aL);

	                //Round 3 
	                a = HH (a, b, c, d, x[5], S31, 0xfffa3942L);
	                d = HH (d, a, b, c, x[8], S32, 0x8771f681L);
	                c = HH (c, d, a, b, x[11], S33, 0x6d9d6122L);
	                b = HH (b, c, d, a, x[14], S34, 0xfde5380cL);
	                a = HH (a, b, c, d, x[1], S31, 0xa4beea44L);
	                d = HH (d, a, b, c, x[4], S32, 0x4bdecfa9L);
	                c = HH (c, d, a, b, x[7], S33, 0xf6bb4b60L);
	                b = HH (b, c, d, a, x[10], S34, 0xbebfbc70L);
	                a = HH (a, b, c, d, x[13], S31, 0x289b7ec6L);
	                d = HH (d, a, b, c, x[0], S32, 0xeaa127faL);
	                c = HH (c, d, a, b, x[3], S33, 0xd4ef3085L);
	                b = HH (b, c, d, a, x[6], S34, 0x4881d05L);
	                a = HH (a, b, c, d, x[9], S31, 0xd9d4d039L);
	                d = HH (d, a, b, c, x[12], S32, 0xe6db99e5L);
	                c = HH (c, d, a, b, x[15], S33, 0x1fa27cf8L);
	                b = HH (b, c, d, a, x[2], S34, 0xc4ac5665L);
					 //Round 4 
	                a = II (a, b, c, d, x[0], S41, 0xf4292244L);
	                d = II (d, a, b, c, x[7], S42, 0x432aff97L);  
	                c = II (c, d, a, b, x[14], S43, 0xab9423a7L); 
	                b = II (b, c, d, a, x[5], S44, 0xfc93a039L);
	                a = II (a, b, c, d, x[12], S41, 0x655b59c3L);
	                d = II (d, a, b, c, x[3], S42, 0x8f0ccc92L); 
	                c = II (c, d, a, b, x[10], S43, 0xffeff47dL);
	                b = II (b, c, d, a, x[1], S44, 0x85845dd1L);
	                a = II (a, b, c, d, x[8], S41, 0x6fa87e4fL);
	                d = II (d, a, b, c, x[15], S42, 0xfe2ce6e0L);
	                c = II (c, d, a, b, x[6], S43, 0xa3014314L);
	                b = II (b, c, d, a, x[13], S44, 0x4e0811a1L);
	                a = II (a, b, c, d, x[4], S41, 0xf7537e82L);
	                d = II (d, a, b, c, x[11], S42, 0xbd3af235L);
	                c = II (c, d, a, b, x[2], S43, 0x2ad7d2bbL);
	                b = II (b, c, d, a, x[9], S44, 0xeb86d391L);

	                register[0] += a; 
	                register[1] += b; 
	                register[2] += c; 
	                register[3] += d; 
	        }
	         
	        //Encode��long���鰴˳����byte���飬��Ϊjava��long������64bit�ģ�ֻ���32bit  
	        private void Encode (byte[] output, long[] input, int len) { 
	                int i, j; 

	                for (i = 0, j = 0; j < len; i++, j += 4) { 
	                        output[j] = (byte)(input[i] & 0xffL); 
	                        output[j + 1] = (byte)((input[i] >>> 8) & 0xffL); 
	                        output[j + 2] = (byte)((input[i] >>> 16) & 0xffL); 
	                        output[j + 3] = (byte)((input[i] >>> 24) & 0xffL); 
	                } 
	        }
			 //Decode��byte���鰴˳��ϳɳ�long���飬��Ϊjava��long������64bit�ģ�ֻ�ϳɵ�32bit����32bit����  
	        private void Decode (long[] out, byte[] in, int len) { 
	                int i, j; 
	                for (i = 0, j = 0; j < len; i++, j += 4) 
	                        out[i] = ignorance(in[j]) | (ignorance(in[j + 1]) << 8) | (ignorance(in[j + 2]) << 16) | (ignorance(in[j + 3]) << 24); 
	        }
	        
	        //ignorance��һ��ʹbyte�����������ţ���Ϊjavaû��unsigned
	        public static long ignorance(byte b) { 
	                return b < 0 ? b & 0x7F + 128 : b; 
	        }
	         
	        //ByteToHEX()��������һ��byte���͵���ת����ʮ�����Ƶ�ASCII��ʾ�� ��Ϊjava�е�byte��toString�޷�ʵ����һ��
	        public static String ByteToHEX(byte in) { 
	                char[] numbers = { '0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' }; 
	                char [] out = new char[2]; 
	                out[0] = numbers[(in >>> 4) & 0X0F]; 
	                out[1] = numbers[in & 0X0F]; 
	                String s = new String(out); 
	                return s; 
	        }
/*
	        public static void main(String args[]) { 
	                MD5 m = new MD5();
	               	System.out.println("��������Ҫ����MD5�任�����ݣ�");
	               	Scanner sc=new Scanner(System.in);
	               	String data;
	               	data=sc.next();
	                System.out.println("MD5(\"" + data + "\")=" +"\n\t"+ m.getMD5(data)); 
	              //RSAǩ�������ڼ���������Ϊ64λ����˼��Ϊ������hash��128λ��λ�������֤ p=7 q=17 e=5 d=77
	                String str=m.getMD5(data);
	                char c[]=new char[32];
	                c=str.toCharArray();
	                 int a=0;
	                for(int i=0;i<32;i++){
	                a=a+c[i];
	                }
	                long s=(long) Math.pow(a, 77)%119;
	                long s1=(long) (Math.pow(a, 5)%119);
	                if(s==s1)System.out.println("ͨ����֤��");
	                else System.out.println("�ܾ�����ǩ����") ; 

	        }*/
}
