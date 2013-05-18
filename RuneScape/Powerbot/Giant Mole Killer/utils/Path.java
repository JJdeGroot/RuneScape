package org.obduro.mole.utils;

import java.util.ArrayList;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.TilePath;

public class Path {
	//private final static Tile[] path = {new Tile(1777, 5235, 0), new Tile(1777, 5234, 0), new Tile(1777, 5233, 0), new Tile(1777, 5232, 0), new Tile(1778, 5231, 0), new Tile(1778, 5230, 0), new Tile(1779, 5229, 0), new Tile(1779, 5228, 0), new Tile(1779, 5227, 0), new Tile(1780,	5226, 0), new Tile(1780, 5225, 0), new Tile(1780, 5224, 0), new Tile(1780, 5223, 0), new Tile(1780, 5222, 0), new Tile(1780, 5221, 0), new Tile(1780, 5220, 0),	new Tile(1780, 5219, 0), new Tile(1781, 5218, 0), new Tile(1781, 5217, 0), new Tile(1781, 5216, 0), new Tile(1781, 5215, 0), new Tile(1781, 5214, 0), new Tile(1781, 5213, 0), new Tile(1781, 5212, 0), new Tile(1781, 5211, 0), new Tile(1781,	5210, 0), new Tile(1781, 5209, 0), new Tile(1781, 5208, 0), new Tile(1780, 5208, 0), new Tile(1779, 5208, 0), new Tile(1778, 5208, 0), new Tile(1777, 5208, 0),	new Tile(1776, 5207, 0), new Tile(1775, 5206, 0), new Tile(1774, 5205, 0), new Tile(1773, 5204, 0), new Tile(1773, 5203, 0), new Tile(1773, 5202, 0), new Tile(1773, 5201, 0), new Tile(1773, 5200, 0), new Tile(1772, 5199, 0), new Tile(1772, 5198, 0), new Tile(1772, 5197, 0), new Tile(1772, 5196, 0), new Tile(1773, 5195, 0), new Tile(1774, 5195, 0), new Tile(1774, 5194, 0), new Tile(1774, 5193, 0), new Tile(1774, 5192, 0), new Tile(1775, 5191, 0), new Tile(1775, 5190, 0), new Tile(1775, 5189, 0), new Tile(1776, 5188, 0), new Tile(1776, 5187, 0), new Tile(1777, 5186, 0), new Tile(1778, 5186, 0), new Tile(1778, 5185, 0), new Tile(1779,	5185, 0), new Tile(1779, 5184, 0), new Tile(1811, 5144, 0), new Tile(1779, 5183, 0), new Tile(1779, 5182, 0), new Tile(1779, 5181, 0), new Tile(1779, 5180, 0),	new Tile(1779, 5179, 0), new Tile(1779, 5178, 0), new Tile(1779, 5177, 0), new Tile(1778, 5176, 0), new Tile(1777, 5175, 0), new Tile(1776, 5175, 0), new Tile(1775, 5175, 0), new Tile(1775, 5174, 0), new Tile(1775, 5173, 0), new Tile(1775, 5172, 0), new Tile(1775, 5171, 0), new Tile(1775, 5170, 0), new Tile(1775, 5169, 0), new Tile(1775, 5168, 0), new Tile(1775, 5167, 0), new Tile(1775, 5166, 0), new Tile(1776, 5165, 0), new Tile(1777, 5165, 0), new Tile(1777, 5164, 0), new Tile(1778, 5163, 0), new Tile(1778, 5162, 0), new Tile(1779, 5162, 0), new Tile(1779, 5161, 0), new Tile(1780, 5160, 0), new Tile(1780, 5159, 0), new Tile(1780,	5158, 0), new Tile(1780, 5157, 0), new Tile(1780, 5156, 0), new Tile(1780, 5155, 0), new Tile(1780, 5154, 0), new Tile(1780, 5153, 0), new Tile(1780, 5152, 0), new Tile(1780, 5151, 0), new Tile(1779, 5151, 0), new Tile(1778, 5151, 0), new Tile(1777, 5151, 0), new Tile(1776, 5151, 0), new Tile(1775, 5151, 0), new Tile(1774, 5151, 0), new Tile(1773, 5150, 0), new Tile(1772, 5149, 0), new Tile(1771, 5148, 0), new Tile(1770, 5148, 0), new Tile(1769, 5148, 0), new Tile(1768, 5149, 0), new Tile(1767, 5149, 0), new Tile(1766, 5149, 0), new Tile(1765, 5149, 0), new Tile(1764, 5149, 0), new Tile(1763, 5149, 0), new Tile(1762, 5149, 0), new Tile(1761, 5149, 0), new Tile(1760, 5149, 0), new Tile(1759, 5149, 0), new Tile(1758, 5149, 0), new Tile(1758, 5150, 0), new Tile(1758, 5151, 0), new Tile(1757, 5151, 0), new Tile(1756, 5151, 0), new Tile(1755, 5151, 0), new Tile(1754, 5151, 0), new Tile(1753, 5151, 0), new Tile(1752, 5151, 0), new Tile(1751, 5151, 0),	new Tile(1750, 5151, 0), new Tile(1749, 5151, 0), new Tile(1748, 5151, 0), new Tile(1747, 5151, 0), new Tile(1746, 5151, 0), new Tile(1745, 5151, 0), new Tile(1705, 5119, 0), new Tile(1744, 5151, 0), new Tile(1743, 5151, 0), new Tile(1742,	5151, 0), new Tile(1742, 5152, 0), new Tile(1742, 5153, 0), new Tile(1741, 5153, 0), new Tile(1740, 5153, 0), new Tile(1739, 5153, 0), new Tile(1738, 5154, 0),	new Tile(1738, 5155, 0), new Tile(1737, 5155, 0), new Tile(1737, 5156, 0), new Tile(1736, 5156, 0), new Tile(1736, 5157, 0), new Tile(1736, 5158, 0), new Tile(1736, 5159, 0), new Tile(1736, 5160, 0), new Tile(1737, 5160, 0), new Tile(1738,	5160, 0), new Tile(1739, 5160, 0), new Tile(1740, 5161, 0), new Tile(1741, 5162, 0), new Tile(1742, 5163, 0), new Tile(1743, 5163, 0), new Tile(1744, 5164, 0),	new Tile(1744, 5165, 0), new Tile(1744, 5166, 0), new Tile(1744, 5167, 0), new Tile(1744, 5168, 0), new Tile(1744, 5169, 0), new Tile(1745, 5170, 0), new Tile(1745, 5171, 0), new Tile(1745, 5172, 0), new Tile(1744, 5173, 0), new Tile(1744, 5174, 0), new Tile(1743, 5174, 0), new Tile(1743, 5175, 0), new Tile(1742, 5175, 0), new Tile(1742, 5176, 0), new Tile(1741, 5176, 0), new Tile(1741, 5177, 0),	new Tile(1740, 5177, 0), new Tile(1740, 5178, 0), new Tile(1740, 5179, 0), new Tile(1740, 5180, 0), new Tile(1740, 5181, 0), new Tile(1740, 5182, 0), new Tile(1740, 5222, 0), new Tile(1740, 5183, 0), new Tile(1740, 5184, 0), new Tile(1740,	5185, 0), new Tile(1740, 5186, 0), new Tile(1740, 5187, 0), new Tile(1740, 5188, 0), new Tile(1740, 5189, 0), new Tile(1740, 5190, 0), new Tile(1740, 5191, 0),	new Tile(1741, 5191, 0), new Tile(1742, 5192, 0), new Tile(1742, 5193, 0), new Tile(1741, 5193, 0), new Tile(1740, 5194, 0), new Tile(1740, 5195, 0), new Tile(1739, 5195, 0), new Tile(1739, 5196, 0), new Tile(1738, 5196, 0), new Tile(1738,	5197, 0), new Tile(1738, 5198, 0), new Tile(1737, 5199, 0), new Tile(1737, 5200, 0), new Tile(1737, 5201, 0), new Tile(1737, 5202, 0), new Tile(1737, 5203, 0),	new Tile(1737, 5204, 0), new Tile(1737, 5205, 0), new Tile(1737, 5206, 0), new Tile(1737, 5207, 0), new Tile(1736, 5208, 0), new Tile(1736, 5209, 0), new Tile(1735, 5209, 0), new Tile(1735, 5210, 0), new Tile(1735, 5211, 0), new Tile(1735, 5212, 0), new Tile(1735, 5213, 0), new Tile(1736, 5214, 0), new Tile(1736, 5215, 0), new Tile(1736, 5216, 0), new Tile(1736, 5217, 0), new Tile(1737, 5218, 0),	new Tile(1738, 5218, 0), new Tile(1739, 5218, 0), new Tile(1740, 5218, 0), new Tile(1741, 5218, 0), new Tile(1742, 5219, 0), new Tile(1743, 5219, 0), new Tile(1744, 5219, 0), new Tile(1745, 5219, 0), new Tile(1745, 5220, 0), new Tile(1746,	5221, 0), new Tile(1747, 5222, 0), new Tile(1755, 5262, 0), new Tile(1748, 5223, 0), new Tile(1749, 5224, 0), new Tile(1749, 5225, 0), new Tile(1750, 5225, 0), new Tile(1751, 5225, 0), new Tile(1752, 5225, 0), new Tile(1753, 5225, 0), new Tile(1754, 5224, 0), new Tile(1755, 5224, 0), new Tile(1755, 5223, 0), new Tile(1756, 5223, 0), new Tile(1756, 5222, 0), new Tile(1757, 5221, 0), new Tile(1758,	5221, 0), new Tile(1758, 5220, 0), new Tile(1759, 5220, 0), new Tile(1760, 5220, 0), new Tile(1760, 5219, 0), new Tile(1761, 5219, 0), new Tile(1762, 5219, 0),	new Tile(1763, 5219, 0), new Tile(1764, 5219, 0), new Tile(1765, 5218, 0), new Tile(1766, 5218, 0), new Tile(1766, 5217, 0), new Tile(1767, 5217, 0), new Tile(1767, 5216, 0), new Tile(1767, 5218, 0), new Tile(1767, 5219, 0), new Tile(1767, 5220, 0), new Tile(1768, 5221, 0), new Tile(1768, 5222, 0), new Tile(1768, 5223, 0), new Tile(1768, 5224, 0), new Tile(1768, 5225, 0), new Tile(1768, 5226, 0), new Tile(1768, 5227, 0), new Tile(1769, 5228, 0), new Tile(1770, 5229, 0), new Tile(1771, 5230, 0), new Tile(1772, 5230, 0), new Tile(1773, 5231, 0), new Tile(1774, 5232, 0), new Tile(1775, 5233, 0), new Tile(1776, 5234, 0), new Tile(1778, 5236, 0)};		
	private final static Tile[] path = {new Tile(1778, 5235, 0), new Tile(1778, 5234, 0), new Tile(1778, 5233, 0), new Tile(1778, 5232, 0), new Tile(1778, 5231, 0), new Tile(1778, 5230, 0), new Tile(1779, 5229, 0), new Tile(1779, 5228, 0), new Tile(1779, 5227, 0), new Tile(1780,5226, 0), new Tile(1780, 5225, 0), new Tile(1780, 5224, 0), new Tile(1780, 5223, 0), new Tile(1780, 5222, 0), new Tile(1780, 5221, 0), new Tile(1780, 5220, 0),new Tile(1780, 5219, 0), new Tile(1780, 5218, 0), new Tile(1780, 5217, 0), new Tile(1780, 5216, 0), new Tile(1780, 5215, 0), new Tile(1780, 5214, 0), new Tile(1780, 5213, 0), new Tile(1781, 5212, 0), new Tile(1781, 5211, 0), new Tile(1780,5210, 0), new Tile(1779, 5209, 0), new Tile(1778, 5208, 0), new Tile(1777, 5207, 0), new Tile(1776, 5206, 0), new Tile(1775, 5205, 0), new Tile(1774, 5204, 0),new Tile(1773, 5203, 0), new Tile(1772, 5202, 0), new Tile(1771, 5202, 0), new Tile(1770, 5202, 0), new Tile(1769, 5202, 0), new Tile(1768, 5201, 0), new Tile(1767, 5201, 0), new Tile(1766, 5201, 0), new Tile(1765, 5201, 0), new Tile(1764,5200, 0), new Tile(1772, 5160, 0), new Tile(1763, 5200, 0), new Tile(1762, 5200, 0), new Tile(1761, 5199, 0), new Tile(1760, 5198, 0), new Tile(1760, 5197, 0),new Tile(1760, 5196, 0), new Tile(1760, 5195, 0), new Tile(1760, 5194, 0), new Tile(1760, 5193, 0), new Tile(1760, 5192, 0), new Tile(1760, 5191, 0), new Tile(1760, 5190, 0), new Tile(1761, 5189, 0), new Tile(1762, 5188, 0), new Tile(1763,5188, 0), new Tile(1763, 5187, 0), new Tile(1764, 5187, 0), new Tile(1764, 5186, 0), new Tile(1764, 5185, 0), new Tile(1764, 5184, 0), new Tile(1764, 5183, 0),new Tile(1764, 5182, 0), new Tile(1764, 5181, 0), new Tile(1764, 5180, 0), new Tile(1764, 5179, 0), new Tile(1764, 5178, 0), new Tile(1765, 5178, 0), new Tile(1766, 5178, 0), new Tile(1767, 5177, 0), new Tile(1768, 5176, 0), new Tile(1769,5176, 0), new Tile(1770, 5176, 0), new Tile(1771, 5175, 0), new Tile(1772, 5174, 0), new Tile(1773, 5174, 0), new Tile(1773, 5173, 0), new Tile(1774, 5172, 0),new Tile(1774, 5171, 0), new Tile(1774, 5170, 0), new Tile(1774, 5169, 0), new Tile(1774, 5168, 0), new Tile(1774, 5167, 0), new Tile(1774, 5166, 0), new Tile(1773, 5166, 0), new Tile(1772, 5166, 0), new Tile(1771, 5166, 0), new Tile(1770,5165, 0), new Tile(1769, 5165, 0), new Tile(1768, 5165, 0), new Tile(1767, 5164, 0), new Tile(1766, 5163, 0), new Tile(1766, 5123, 0), new Tile(1765, 5162, 0),new Tile(1764, 5161, 0), new Tile(1763, 5160, 0), new Tile(1762, 5159, 0), new Tile(1762, 5158, 0), new Tile(1762, 5157, 0), new Tile(1761, 5156, 0), new Tile(1760, 5155, 0), new Tile(1759, 5154, 0), new Tile(1758, 5153, 0), new Tile(1757,5153, 0), new Tile(1756, 5153, 0), new Tile(1755, 5153, 0), new Tile(1754, 5153, 0), new Tile(1753, 5153, 0), new Tile(1752, 5153, 0), new Tile(1751, 5153, 0),new Tile(1750, 5153, 0), new Tile(1749, 5153, 0), new Tile(1748, 5153, 0), new Tile(1747, 5153, 0), new Tile(1746, 5153, 0), new Tile(1745, 5153, 0), new Tile(1744, 5153, 0), new Tile(1743, 5153, 0), new Tile(1742, 5152, 0), new Tile(1741,5151, 0), new Tile(1740, 5150, 0), new Tile(1740, 5151, 0), new Tile(1740, 5152, 0), new Tile(1740, 5153, 0), new Tile(1740, 5154, 0), new Tile(1740, 5155, 0),new Tile(1740, 5156, 0), new Tile(1739, 5157, 0), new Tile(1738, 5158, 0), new Tile(1738, 5159, 0), new Tile(1739, 5160, 0), new Tile(1740, 5161, 0), new Tile(1741, 5162, 0), new Tile(1742, 5163, 0), new Tile(1743, 5164, 0), new Tile(1744,5165, 0), new Tile(1744, 5166, 0), new Tile(1745, 5167, 0), new Tile(1746, 5168, 0), new Tile(1747, 5169, 0), new Tile(1747, 5170, 0), new Tile(1747, 5171, 0),new Tile(1748, 5172, 0), new Tile(1749, 5173, 0), new Tile(1750, 5174, 0), new Tile(1751, 5175, 0), new Tile(1752, 5176, 0), new Tile(1753, 5177, 0), new Tile(1753, 5178, 0), new Tile(1753, 5179, 0), new Tile(1754, 5180, 0), new Tile(1754,5181, 0), new Tile(1754, 5182, 0), new Tile(1754, 5183, 0), new Tile(1754, 5184, 0), new Tile(1754, 5185, 0), new Tile(1754, 5186, 0), new Tile(1755, 5187, 0),new Tile(1755, 5188, 0), new Tile(1755, 5189, 0), new Tile(1747, 5229, 0), new Tile(1756, 5190, 0), new Tile(1756, 5191, 0), new Tile(1756, 5192, 0), new Tile(1756, 5193, 0), new Tile(1756, 5194, 0), new Tile(1757, 5195, 0), new Tile(1757,5196, 0), new Tile(1757, 5197, 0), new Tile(1757, 5198, 0), new Tile(1757, 5199, 0), new Tile(1757, 5200, 0), new Tile(1757, 5201, 0), new Tile(1757, 5202, 0),new Tile(1756, 5203, 0), new Tile(1756, 5204, 0), new Tile(1755, 5204, 0), new Tile(1755, 5205, 0), new Tile(1754, 5205, 0), new Tile(1753, 5205, 0), new Tile(1752, 5205, 0), new Tile(1751, 5205, 0), new Tile(1750, 5205, 0), new Tile(1749,5206, 0), new Tile(1748, 5207, 0), new Tile(1748, 5208, 0), new Tile(1747, 5208, 0), new Tile(1747, 5209, 0), new Tile(1746, 5209, 0), new Tile(1746, 5210, 0),new Tile(1745, 5210, 0), new Tile(1745, 5209, 0), new Tile(1744, 5209, 0), new Tile(1743, 5209, 0), new Tile(1742, 5209, 0), new Tile(1741, 5209, 0), new Tile(1740, 5209, 0), new Tile(1740, 5210, 0), new Tile(1740, 5211, 0), new Tile(1740,5212, 0), new Tile(1740, 5213, 0), new Tile(1740, 5214, 0), new Tile(1740, 5215, 0), new Tile(1740, 5216, 0), new Tile(1740, 5217, 0), new Tile(1741, 5218, 0),new Tile(1742, 5219, 0), new Tile(1743, 5219, 0), new Tile(1744, 5219, 0), new Tile(1745, 5219, 0), new Tile(1746, 5219, 0), new Tile(1747, 5220, 0), new Tile(1748, 5220, 0), new Tile(1749, 5220, 0), new Tile(1750, 5221, 0), new Tile(1751,5221, 0), new Tile(1752, 5221, 0), new Tile(1753, 5221, 0), new Tile(1754, 5221, 0), new Tile(1755, 5221, 0), new Tile(1756, 5221, 0), new Tile(1757, 5221, 0),new Tile(1758, 5220, 0), new Tile(1759, 5220, 0), new Tile(1760, 5219, 0), new Tile(1761, 5219, 0), new Tile(1761, 5218, 0), new Tile(1762, 5218, 0), new Tile(1762, 5217, 0), new Tile(1763, 5217, 0), new Tile(1763, 5216, 0), new Tile(1764,5217, 0), new Tile(1765, 5218, 0), new Tile(1766, 5219, 0), new Tile(1767, 5220, 0), new Tile(1767, 5221, 0), new Tile(1768, 5222, 0), new Tile(1768, 5223, 0),new Tile(1768, 5224, 0), new Tile(1769, 5225, 0), new Tile(1770, 5226, 0), new Tile(1771, 5227, 0), new Tile(1772, 5228, 0), new Tile(1773, 5229, 0), new Tile(1774, 5230, 0), new Tile(1775, 5230, 0), new Tile(1799, 5270, 0), new Tile(1776, 5231, 0), new Tile(1776, 5232, 0), new Tile(1776, 5233, 0)};

	public static TilePath createThePath(){
		ArrayList<Tile> list = new ArrayList<Tile>();
		
		while(list.size() < 1000){
			Tile myPos = LocalPlayer.getTile();
			if(!list.contains(myPos)){
				list.add(LocalPlayer.getTile());
				System.out.println("------------");
				for(int i = 0; i < list.size(); i++){
					Tile tile = list.get(i);
					System.out.print("new Tile(" + tile.getX() + ", " + tile.getY() + ", " + tile.getPlane() + "), ");
				}
				System.out.println("");
				System.out.println("------------");
			}else{
				Task.sleep(10);
			}
		}
		
		return null;
	}
	
	// Gets the tile that is the nearest to our location
	private static int getNearestInPath(){
		Tile myPos = LocalPlayer.getTile();
		int index = 0;
		double nearest = 10000;
		for(int i = 0; i < path.length; i++){
			double distance = path[i].distance(myPos);
			if(distance < nearest){
				nearest = distance;
				index = i;
			}
		}
		
		System.out.println("Nearest tile = " + path[index] + " with: " + nearest + " & index: " + index);
		return index;
	}
	
	// Adds randomization to a Tile
	private static Tile randomizeTile(Tile tile, int width, int height){
		return new Tile(tile.getX() + Random.nextInt(-width, width), tile.getY() + Random.nextInt(-height, height), tile.getPlane());
	}
	
	// Constructs a new path, our position is index 0.
	private static Tile[] constructPath(){
		int index = getNearestInPath();
		Tile[] newPath = new Tile[path.length];
		int i = index;
		while(i < path.length){
			newPath[i-index] = randomizeTile(path[i], 2, 2);
			i++;
		}
		int j = 0;
		while(j < index){
			newPath[path.length-index+j] = randomizeTile(path[j], 2, 2);
			j++;
		}
		return newPath;
	}
	
	// Finds the best tile in a path
	private static Tile findTile(Tile[] path){
		Tile myPos = LocalPlayer.getTile();
		double largestDistance = 0;
		int tileIndex = 0;
		int maxTileDistance = Random.nextInt(15, 20);
		
		for(int i = 0; i < path.length; i++){
			double distance = path[i].distance(myPos);
			if(distance < maxTileDistance){
				if(distance > largestDistance){
					largestDistance = distance;
					tileIndex = i;
				}
			}else{
				if(tileIndex > 0){
					break;
				}
			}
		}
		
		return path[tileIndex];
	}
	
	// Walks to the best tile possible
	public static void walk(){
		Tile tile = findTile(constructPath());
		if(tile.clickOnMap()){
			System.out.println("Clicked");
			LocalPlayer.wait(tile);
		}

	}

}
