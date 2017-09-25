package restapi.team1;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

@Path("/video")
public class VideoStream {
	final int chunk_size = 1024 * 1024; // 1MB chunks
	final String path = "C:/data/";
	final String contentName = "mhworld";
	final String videoType = ".mp4";

	/*
	 ** Basic video transfer
	@Path("{s}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response video(@PathParam("s") String s) {
	    File video = new File(path+contentName+s+videoType);
	    //File file = new File("C:/Data/video.mp4");
	    return Response.ok(video, MediaType.APPLICATION_OCTET_STREAM)
	            .build();
	}*/

	@Path("{s}")
    @GET
    @Produces("video/mp4")
	public Response streamAudio(@PathParam("s") String s,@HeaderParam("Range") String range) throws Exception{
		if(!validateString(s)) {
			System.out.println("Validate string failed on : " + s);
			throw new RuntimeException("Invalid File Name : " + s);
		}
		File video = new File(path + contentName + s + videoType);
		if(!video.isFile()) {
			System.out.println("Not a valid file : " + s);
			throw new RuntimeException("Invalid File Name : " + s);
		}
		Response response =  buildStream(video, range);
		return response;
	}

    private Response buildStream(final File asset, final String range) throws Exception {
        // range not requested : Firefox, Opera, IE do not send range headers
        if (range == null) {
            StreamingOutput streamer = new StreamingOutput() {
                @Override
                public void write(final OutputStream output) throws IOException, WebApplicationException {

                    final FileChannel inputChannel = new FileInputStream(asset).getChannel();
                    final WritableByteChannel outputChannel = Channels.newChannel(output);
                    try {
                        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    } finally {
                        // closing the channels
                        inputChannel.close();
                        outputChannel.close();
                    }
                }
            };
            return Response.ok(streamer).header(HttpHeaders.CONTENT_LENGTH, asset.length()).build();
        }

        String[] ranges = range.split("=")[1].split("-");
        final int from = Integer.parseInt(ranges[0]);
        /**
         * Chunk media if the range upper bound is unspecified. Chrome sends "bytes=0-"
         */
        int to = chunk_size + from;
        if (to >= asset.length()) {
            to = (int) (asset.length() - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }

        final String responseRange = String.format("bytes %d-%d/%d", from, to, asset.length());
        final RandomAccessFile raf = new RandomAccessFile(asset, "r");
        raf.seek(from);

        final int length = to - from + 1;
        final MediaStreamer streamer = new MediaStreamer(length, raf);
				//206 is for partial content
        Response.ResponseBuilder res = Response.status(206).entity(streamer)
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", responseRange)
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
                .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));
        return res.build();
    }

    private boolean validateString(String content) {
    	if(content.length() > 10) {
    		return false;
    	}

    	boolean isAllLetters = content.chars().allMatch(Character::isLetter);
    	return isAllLetters;
    }
}
