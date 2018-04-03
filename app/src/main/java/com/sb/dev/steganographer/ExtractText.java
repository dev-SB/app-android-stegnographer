package com.sb.dev.steganographer;

import android.graphics.Bitmap;
import android.util.Log;


public class ExtractText
    {
        public static StringBuilder extract(Bitmap encryptedImage1)
            {
                Bitmap encryptedImage=encryptedImage1.copy(Bitmap.Config.ARGB_8888,true);
                int x = 0, y = 0, bitValue;
                final int EXTRACTOR = 0x00000001;
                final int ONEATSTART=0x80;
                char chars;
                int asciiCode = 0;
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; ; i++)
                    {
                        for (int j = 0; j < 8; j++)
                            {
                                bitValue = encryptedImage.getPixel(x, y) & EXTRACTOR;//Extracts last bit from blue color.
                                x++;
                                if (x > encryptedImage.getWidth())
                                    {
                                        x = 0;
                                        y++;
                                    }
                                asciiCode = asciiCode >> 1;//Left shift to form the character moving the bits by one place and store a new bit.
                                if (bitValue == 1)
                                    {
                                        asciiCode = asciiCode | ONEATSTART;//Replaces bit value with 1

                                    }
                            }
                        if (asciiCode == 0)//Checks for ascii value 1 marking end of the text.
                            {
                                break;
                            }
                        chars = (char) asciiCode;

                        stringBuilder.append(chars);//for appending characters at the end of the previous characters to form a string.
                    }
                Log.d("extract text:","done");
                return stringBuilder;
            }
    }
