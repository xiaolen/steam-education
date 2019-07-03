package com.example.steam.crwal.link;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
public class Links {

    private static LinkedList website_Links = new LinkedList();//访问的网站

    private static Set<String> accessing_Abnormal_Web_Sites = new HashSet<>();//访问不正常的网站

    private static Set<String> complete_Website_Link = new HashSet<>();//访问完成的网站

    private static Set links_visited = new HashSet();//访问的链接

    private static LinkedList links_not_visited = new LinkedList();//访问异常的链接

    private static Set<String> access_complete_links = new HashSet<>();//访问完成的链接

    public static int accessingAbonrmalWebSitesSize() {
        return accessing_Abnormal_Web_Sites.size();
    }

    public static void addAccessingAbnormalWebSites(String url) {
        accessing_Abnormal_Web_Sites.add(url);
    }

    public static void addAccessCompleteLinks(String url) {
        access_complete_links.add(url);
    }

    public static int accessCompleteLinksSize() {
        return access_complete_links.size();
    }

    public static void addCompleteWebsiteLink(Object url) {
        complete_Website_Link.add(url.toString());
    }

    public static int getCompleteWebsiteLinkSize() {
        return complete_Website_Link.size();
    }

    public static void addLinksNotVisited(String url) {
        if (url != null && !url.trim().equals("") && !links_visited.contains(url) && !links_not_visited.contains(url)) {
            links_not_visited.add(url);
        }
    }

    public static int getWebsiteLinksSize() {
        return website_Links.size();
    }

    public static void addWebsiteLinks(String url) {
        website_Links.addFirst(url);
    }

    public static int linksNotVisitedSize() {
        return links_not_visited.size();
    }

    public static Set fetchLinksVisited() {
        return links_visited;
    }

    public static LinkedList getWebsiteLinks() {
        return website_Links;
    }

    public static void addLinksVisited(String url) {
        links_visited.add(url);
    }

    public static Set<String> getCompleteWebsiteLink() {
        return complete_Website_Link;
    }

    public static void removeWebsiteLinks() {
        website_Links.removeFirst();
    }

    public static void removeWebsiteLinks(Object url) {
        website_Links.remove(url);
    }

    public static int getLinksVisited() {
        return links_visited.size();
    }

    public static void removeLinksVisited(String url) {
        links_visited.remove(url);
    }

    public static LinkedList getLinksNotVisited() {
        return links_not_visited;
    }

    public static Object removeLinksNotVisited() {
        return links_not_visited.removeFirst();
    }

    public static boolean linksNotVisitedIsNull() {
        return links_not_visited.isEmpty();
    }

}
