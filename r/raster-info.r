#takes <input raste> <info output file> and summarizes raster file geospatial info into output file
library(sf)
library(sp)
library(raster)

args <- commandArgs(TRUE)
inputRaster <- args[1]
resultOutput <- args[2]


r = raster(inputRaster)

res = as.data.frame(rbind(	c("nrows",r@nrows),#number of rows
							c("ncols",r@ncols),#number of columsn
							c("xmin",r@extent@xmin),
							c("xmax",r@extent@xmax),
							c("ymin",r@extent@ymin),
							c("ymax",r@extent@ymax)))							
							
							
colnames(res) = c("attribute","value") 													

write.csv(res,resultOutput,row.names=FALSE)
