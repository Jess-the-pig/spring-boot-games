package henrotaym.env.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import henrotaym.env.entities.Game;
import henrotaym.env.http.resources.CoverResource;
import henrotaym.env.http.resources.GameResource;
import henrotaym.env.http.resources.StudioResource;
import henrotaym.env.http.resources.TagResource;
import henrotaym.env.mappers.CoverMapper;
import henrotaym.env.mappers.GameMapper;
import henrotaym.env.mappers.StudioMapper;
import henrotaym.env.mappers.TagMapper;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GameSerializer {
  private final HasIncludablesSerializer hasIncludablesSerializer;
  private final GameMapper gameMapper;
  private final TagMapper tagMapper;
  private final CoverMapper coverMapper;
  private final StudioMapper studioMapper;

  public String serialize(Game game, Set<String> include) throws JsonProcessingException {
    GameResource gameResource = this.gameMapper.resource(game);

    if (include.contains("studio")) {
      StudioResource studioResource = this.studioMapper.resource(game.getStudio());
      gameResource.setStudio(studioResource);
    }

    if (include.contains("tags")) {
      List<TagResource> tagResources =
          game.getTags().stream().map(tag -> this.tagMapper.resource(tag)).toList();
      gameResource.setTags(tagResources);
    }

    if (include.contains("cover") && game.getCover() != null) {
      CoverResource coverResource = this.coverMapper.resource(game.getCover());
      gameResource.setCover(coverResource);
    }

    return this.hasIncludablesSerializer.serialize(gameResource, include);
  }
  
}
