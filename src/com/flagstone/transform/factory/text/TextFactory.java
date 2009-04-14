/*
 *  TextConstructor.java
 *  Transform Utilities
 *
 * Copyright (c) 2001-2008 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its contributors 
 *    may be used to endorse or promote products derived from this software 
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package com.flagstone.transform.factory.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.flagstone.transform.factory.font.Font;
import com.flagstone.transform.movie.datatype.Bounds;
import com.flagstone.transform.movie.datatype.Color;
import com.flagstone.transform.movie.datatype.ColorTable;
import com.flagstone.transform.movie.datatype.CoordTransform;
import com.flagstone.transform.movie.font.DefineFont2;
import com.flagstone.transform.movie.text.DefineText2;
import com.flagstone.transform.movie.text.DefineTextField;
import com.flagstone.transform.movie.text.GlyphIndex;
import com.flagstone.transform.movie.text.TextSpan;

/**
 * Text is used to define static and dynamic text fields that can be added to 
 * a Flash file.
 *
 * @see Font
 */
public final class TextFactory
{
	/**
	 * Create a bound box that encloses the line of text when rendered using 
	 * the specified font and size.
	 * 
	 * @param text the string to be displayed.

     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
	 *
	 * @return the bounding box that completely encloses the text.
	 */
    public static Bounds boundsForText(String text, DefineFont2 font, int size)
    {
        List<Integer>codes = font.getCodes();
        List<Integer>advances = font.getAdvances();
       
        float scale = size/1024.0f;
        int advance = 0;

        for (int i=0; i<text.length(); i++)
        {
    	    for (int j=0; j<codes.size(); j++)
    	    {
    		    if (text.charAt(i) == codes.get(j)) 
    		    {
    	            advance += (int)(advances.get(j)*scale);    			   
    		    }
    	    }
        }
        return new Bounds(0, -(int)(font.getAscent()/scale), advance, (int)(font.getDescent()/scale));
    }

    /**
     * Create an array of characters that can be added to a text span.
     * 
     * @param text the string to be displayed.
     * 
     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
     *
     * @return a TextSpan object that can be added to a DefineText or DefineText2 object.
     */
    public static List<GlyphIndex> charactersForText(String text, DefineFont2 font, int size)
    {
        List<GlyphIndex>characters = new ArrayList<GlyphIndex>(text.length());
        List<Integer>codes = font.getCodes();
        List<Integer>advances = font.getAdvances();
       
        float scale = size/1024.0f;

        for (int i=0; i<text.length(); i++)
        {
    	    for (int j=0; j<codes.size(); j++)
    	    {
    		    if (text.charAt(i) == codes.get(j)) 
    		    {
    	            characters.add(new GlyphIndex(j, (int)(advances.get(j)*scale)));    			   
    		    }
    	    }
        }
        return characters;
    }

    /**
     * Create a span of text that can be added to a static text field.
     * 
     * @param text the string to be displayed.
     * 
     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
     * 
     * @param color the colour used to display the text.
     * 
     * @return a TextSpan object that can be added to a DefineText or DefineText2 object.
     */
	public static TextSpan defineSpan(String text, DefineFont2 font, int size, Color color)
    {
        float scale = size/1024.0f;
        
        int xCoord = 0;
        int yCoord = (int)(font.getAscent()/scale);

        return new TextSpan(font.getIdentifier(), size, color, xCoord, yCoord, charactersForText(text, font, size));
    }

    /**
     * Create a definition for a static text field that displays a single line 
     * of text in the specified font.
     * 
     * @param uid the unique identifier that will be used to reference the text
     * field in a flash file.
     * 
     * @param text the string to be displayed.
     * 
     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
     * 
     * @param color the colour used to display the text.
     * 
     * @return a DefineText2 object that can be added to a Flash file.
     */
    public static DefineText2 defineText(int uid, String text, DefineFont2 font, int size, Color color)
    {
        CoordTransform transform = new CoordTransform(1.0f, 1.0f, 0.0f, 0.0f, 0, 0);
        ArrayList<TextSpan> spans = new ArrayList<TextSpan>();

        spans.add(defineSpan(text, font, size, color));

        return new DefineText2(uid, boundsForText(text, font, size), transform, spans);
    }

    /**
     * Create a definition for a static text field that displays a block of
     * text in the specified font.
     * 
     * @param uid the unique identifier that will be used to reference the text
     * field in a flash file.
     * 
     * @param lines the array of strings to be displayed.
     * 
     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
     * 
     * @param color the colour used to display the text.
     * 
     * @return a DefineText2 object that can be added to a Flash file.
     */
    public static DefineText2 defineTextBlock(int uid, List<String> lines, DefineFont2 font, int size, Color color, int lineSpacing)
    {
        CoordTransform transform = new CoordTransform(1.0f, 1.0f, 0.0f, 0.0f, 0, 0);
        float scale = size/1024.0f;
        
        int xMin = 0;
        int yMin = 0;
        int xMax = 0;
        int yMax = 0;
        
        int xOffset = 0;
        int yOffset = (int)(font.getAscent()/scale);
        
        ArrayList<TextSpan> spans = new ArrayList<TextSpan>();
        String text;
        
        int lineNumber = 0;

        for (Iterator<String> i = lines.iterator(); i.hasNext(); yOffset += lineSpacing, lineNumber++)
        {
            text = i.next();

            spans.add(new TextSpan(font.getIdentifier(), size, color, xOffset, yOffset, charactersForText(text, font, size)));

            Bounds bounds = boundsForText(text, font, size);
            
            if (lineNumber==0) {
                yMin = bounds.getMinY();
                yMax = bounds.getMaxY();
            }
            else {
                yMax += lineSpacing;
            }
            
            if (lineNumber==lines.size()-1) {
                yMax += bounds.getHeight();
            }
            
            xMin = (xMin < bounds.getMinX()) ? xMin : bounds.getMinX();
            xMax = (xMax > bounds.getMaxX()) ? xMax : bounds.getMaxX();
        }

        return new DefineText2(uid, new Bounds(xMin, yMin, xMax, yMax), transform, spans);
    }
    
    /**
     * Create a definition for a dynamic text field that displays a string in
     * the specified font.
     * 
     * @param uid the unique identifier that will be used to reference the text
     * field in a flash file.
     * 
     * @param text the string to be displayed.
     * 
     * @param font the font used to display the text.
     * 
     * @param size the size of the font in twips.
     * 
     * @param color the colour used to display the text.
     * 
     * @return a DefineTextField object that can be added to a Flash file.
     */
    public static DefineTextField defineTextField(int uid, String text, DefineFont2 font, int size, Color color)
    {
    	DefineTextField field = new DefineTextField(uid, boundsForText(text, font, size), "", text);

    	field.setUseFontGlyphs(true);
        field.setFontIdentifier(font.getIdentifier());
        field.setFontHeight(size);
        field.setColor(ColorTable.black());
 
        return field;
    }


}
