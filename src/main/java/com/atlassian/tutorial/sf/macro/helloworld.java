/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlassian.tutorial.sf.macro;

/**
 *
 * @author sf
 */
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.DefaultPageManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.DefaultSpaceManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import java.nio.file.Paths;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.List;

@Scanned
public class helloworld implements Macro {

    private SpaceManager spaceManager;
    private PageManager pageManager;
    private PageBuilderService pageBuilderService;

    @Autowired
    public helloworld(@ComponentImport PageBuilderService pageBuilderService,
                      @ComponentImport SpaceManager spaceManager,
                      @ComponentImport PageManager pageManager) {
        this.pageBuilderService = pageBuilderService;
        this.pageManager = pageManager;
        this.spaceManager = spaceManager;
    }
    
    public String execute(Map<String, String> map, String s, 
            ConversionContext conversionContext) 
            throws MacroExecutionException {
        
        
        
        String key = conversionContext.getSpaceKey();
        Space space = spaceManager.getSpace(key);
        List<Page> pages = pageManager.getPages(space, true);
        
        pageBuilderService.assembler().resources()
            .requireWebResource("com.atlassian.tutorial.myConfluenceMacro:"
                                + "myConfluenceMacro-resources");
        StringBuilder output = new StringBuilder("<div class =\"helloworld\">");
        output.append("<div class = \"").append(map.get("Color"))
            .append(" style = \"color: ").append(map.get("Color")).append("\"")
            .append("\">");
        if (map.get("Name") != null) {
            output.append("<h1>Hello ").append(map.get("Name"))
                    .append("!</h1>");
        } else {
            output.append("<h1>Hello World!<h1>");
        }
        output.append("<table><tr><th>Page</th><th>Version</th></tr>");
        pages.sort((Page p, Page p1) -> p.getVersion() < p1.getVersion() 
                ? 1 : -1);
        pages.stream().forEach(page -> output
            .append("<tr><td><a href=\"")
                .append(Paths.get(page.getUrlPath()).getFileName())
                .append("\">")
                .append(page.getTitle()).append("</a>").append("</td>")
            .append("<td>").append(page.getVersion()).append("</td></tr>")
        );
        output.append("</table>");
        output.append("</div>" + "</div>");
        return output.toString();
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
